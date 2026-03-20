package com.scribble.it.feature_canvas.presentation.canvasrecycle.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.scribble.it.feature_canvas.domain.error.CanvasError
import com.scribble.it.feature_canvas.domain.model.canvasSummary.CanvasSummary
import com.scribble.it.feature_canvas.domain.model.operation.ArchiveAction
import com.scribble.it.feature_canvas.domain.model.operation.DeleteRequest
import com.scribble.it.feature_canvas.domain.model.usecaseResult.ArchiveUseCaseResult
import com.scribble.it.feature_canvas.domain.model.usecaseResult.DeleteUseCaseResult
import com.scribble.it.feature_canvas.domain.repository.CanvasRepository
import com.scribble.it.feature_canvas.domain.result.Result
import com.scribble.it.feature_canvas.domain.usecase.ArchiveCanvasesUseCase
import com.scribble.it.feature_canvas.domain.usecase.DeleteCanvasesUseCase
import com.scribble.it.feature_canvas.domain.usecase.GetPagingCanvasesUseCase
import com.scribble.it.feature_canvas.presentation.common.action.BulkRecycleActionType
import com.scribble.it.feature_canvas.presentation.canvasrecycle.action.CanvasRecycleAction
import com.scribble.it.feature_canvas.presentation.canvasrecycle.event.CanvasRecycleEvent
import com.scribble.it.feature_canvas.presentation.canvasrecycle.state.CanvasRecycleUiState
import com.scribble.it.feature_canvas.presentation.common.action.BulkActionEvent
import com.scribble.it.feature_canvas.presentation.common.action.CanvasItemClickType
import com.scribble.it.feature_canvas.presentation.common.dialog.state.ConfirmationDialogState
import com.scribble.it.feature_canvas.presentation.common.state.TopBarMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CanvasRecycleViewModel @Inject constructor(
    private val canvasRepository: CanvasRepository,
    private val getPagingCanvasesUseCase: GetPagingCanvasesUseCase,
    private val deleteCanvasesUseCase: DeleteCanvasesUseCase,
    private val archiveCanvasesUseCase: ArchiveCanvasesUseCase
) : ViewModel() {

    private val _canvasRecycleUiState: MutableStateFlow<CanvasRecycleUiState> =
        MutableStateFlow(CanvasRecycleUiState())
    val canvasRecycleUiState: StateFlow<CanvasRecycleUiState> = _canvasRecycleUiState.asStateFlow()

    private val eventChannel = Channel<CanvasRecycleEvent>(Channel.BUFFERED)
    val eventsFlow: Flow<CanvasRecycleEvent> = eventChannel.receiveAsFlow()

    private val refreshTrigger = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    @OptIn(ExperimentalCoroutinesApi::class)
    val recyclePagingFlow: Flow<PagingData<CanvasSummary>> =
        refreshTrigger
            .onStart { emit(Unit) }
            .flatMapLatest {
                getPagingCanvasesUseCase(
                    query = "",
                    sortOption = "DELETED_DATE_DESC",
                    isRecycled = true
                )
            }
            .cachedIn(viewModelScope)

    init {
        loadCanvasViewMode()

        _canvasRecycleUiState.update { it.copy(isInitialLoading = true) }
        viewModelScope.launch {
            // Wait until first Paging load completes
            recyclePagingFlow.collectLatest {
                delay(2000)
                _canvasRecycleUiState.update { state ->
                    state.copy(isInitialLoading = false, isRefreshing = false)
                }
            }
        }
    }

    private fun loadCanvasViewMode() {
        viewModelScope.launch {
            canvasRepository.getScribbleViewMode()
                .collectLatest { mode ->
                    _canvasRecycleUiState.update { state ->
                        state.copy(canvasViewMode = mode)
                    }
                }
        }
    }


    fun viewAction(action: CanvasRecycleAction) {
        when (action) {

            is CanvasRecycleAction.CanvasItemInteractionAction -> {
                val interaction = action.interaction
                when(interaction.clickType) {
                    CanvasItemClickType.CLICK -> {
                        val topBarMode = _canvasRecycleUiState.value.topBarMode
                        if (topBarMode == TopBarMode.DELETE) {
                            toggleSelectedItems(id = interaction.selectedId, availableItemIds = interaction.availableItemIds)
                        } else { Unit }
                    }
                    CanvasItemClickType.LONG_CLICK -> {
                        toggleSelectedItems(id = interaction.selectedId, availableItemIds = interaction.availableItemIds)
                    }
                }
            }


            is CanvasRecycleAction.CloseDeleteBar -> {
                _canvasRecycleUiState.update { state ->
                    state.copy(
                        topBarMode = TopBarMode.DEFAULT,
                        selectedIds = emptySet()
                    )
                }
            }

            is CanvasRecycleAction.RefreshData -> {
                val state = _canvasRecycleUiState.value
                if (state.isInitialLoading || state.topBarMode == TopBarMode.SEARCH) {
                    return
                }

                viewModelScope.launch {
                    _canvasRecycleUiState.update { state ->
                        state.copy(
                            isInitialLoading = true,
                            isRefreshing = true
                        )
                    }

                    refreshTrigger.emit(Unit)
                }
            }

            is CanvasRecycleAction.SelectAllCanvases -> {
                val state = _canvasRecycleUiState.value

                val ids = action.targetIds
                val allSelected = state.selectedIds == ids

                _canvasRecycleUiState.update {
                    it.copy(
                        selectedIds = if (allSelected) emptySet() else ids,
                        isAllSelected = !allSelected
                    )
                }
            }

            is CanvasRecycleAction.Bulk -> {
                when(val event = action.event) {
                    is BulkActionEvent.Confirm -> {
                        val dialog = _canvasRecycleUiState.value.dialogState

                        viewModelScope.launch {
                            val targetIds = dialog.targetIds

                            when (dialog.actionType) {
                                BulkRecycleActionType.DELETE_ALL ->
                                    deleteCanvasesUseCase(
                                        deleteRequest = DeleteRequest.CanvasesById(targetIds)
                                    ).collectLatest { result: Result<DeleteUseCaseResult, CanvasError> ->
                                        when (result) {
                                            is Result.Error -> Log.d("CanvasDelete", result.error.toString())
                                            is Result.Loading -> Log.d("CanvasDelete", "LOADING")
                                            is Result.Success -> Log.d("CanvasDelete", "Deleted Successfully")
                                        }
                                    }

                                BulkRecycleActionType.RESTORE_ALL ->
                                    archiveCanvasesUseCase(
                                        canvasIds = targetIds,
                                        archiveAction = ArchiveAction.RESTORE
                                    ).collectLatest { result: Result<ArchiveUseCaseResult, CanvasError> ->
                                        when (result) {
                                            is Result.Error -> Log.d("CanvasArchive", result.error.toString())
                                            is Result.Loading -> Log.d("CanvasArchive", "LOADING")
                                            is Result.Success -> {
                                                Log.d("CanvasArchive", "Result: ${result.data}")
                                                _canvasRecycleUiState.update { state ->
                                                    state.copy(
                                                        topBarMode = TopBarMode.DEFAULT,
                                                        selectedIds = emptySet()
                                                    )
                                                }
                                            }
                                        }
                                    }

                                else -> return@launch
                            }

                            _canvasRecycleUiState.update {
                                it.copy(dialogState = ConfirmationDialogState())
                            }
                        }
                    }

                    is BulkActionEvent.Dismiss -> {
                        _canvasRecycleUiState.update {
                            it.copy(dialogState = ConfirmationDialogState())
                        }
                    }

                    is BulkActionEvent.Request -> {
                        val (title, message) = when (event.type) {
                            BulkRecycleActionType.DELETE_ALL ->
                                "Delete Forever" to "This cannot be undone."

                            BulkRecycleActionType.RESTORE_ALL ->
                                "Restore" to "Items will be restored."

                            else -> return
                        }

                        if(event.targetIds.isNotEmpty()) {
                            _canvasRecycleUiState.update {
                                it.copy(
                                    dialogState = ConfirmationDialogState(
                                        isVisible = true,
                                        title = title,
                                        message = message,
                                        actionType = event.type,
                                        targetIds = event.targetIds
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun toggleSelectedItems(id: Long, availableItemIds: Set<Long>) {
        _canvasRecycleUiState.update { state ->
            val newSelectedIds = if (state.selectedIds.contains(id)) {
                state.selectedIds - id
            } else {
                state.selectedIds + id
            }

            val allSelected = newSelectedIds == availableItemIds

            state.copy(
                selectedIds = newSelectedIds,
                isAllSelected = allSelected,
                topBarMode = if (newSelectedIds.isNotEmpty()) TopBarMode.DELETE else TopBarMode.DEFAULT
            )
        }
    }
}
package com.scribble.it.feature_canvas.presentation.canvaslist.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.cachedIn
import com.scribble.it.feature_canvas.domain.error.CanvasError
import com.scribble.it.feature_canvas.domain.model.canvasSummary.CanvasSummary
import com.scribble.it.feature_canvas.domain.model.operation.ArchiveAction
import com.scribble.it.feature_canvas.domain.model.usecaseResult.ArchiveUseCaseResult
import com.scribble.it.feature_canvas.domain.repository.CanvasRepository
import com.scribble.it.feature_canvas.domain.result.Result
import com.scribble.it.feature_canvas.domain.usecase.ArchiveCanvasesUseCase
import com.scribble.it.feature_canvas.domain.usecase.GetPagingCanvasesUseCase
import com.scribble.it.feature_canvas.presentation.canvasdraw.navigation.CanvasDrawRoute
import com.scribble.it.feature_canvas.presentation.canvaslist.action.CanvasListAction
import com.scribble.it.feature_canvas.presentation.canvaslist.event.CanvasListEvent
import com.scribble.it.feature_canvas.presentation.canvaslist.state.CanvasListUiState
import com.scribble.it.feature_canvas.presentation.canvaslist.state.PaneState
import com.scribble.it.feature_canvas.presentation.common.action.BulkActionEvent
import com.scribble.it.feature_canvas.presentation.common.action.BulkRecycleActionType
import com.scribble.it.feature_canvas.presentation.common.action.CanvasItemClickType
import com.scribble.it.feature_canvas.presentation.common.dialog.state.ConfirmationDialogState
import com.scribble.it.feature_canvas.presentation.common.state.CanvasViewMode
import com.scribble.it.feature_canvas.presentation.common.state.TopBarMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CanvasListViewModel @Inject constructor(
    private val canvasRepository: CanvasRepository,
    private val getPagingCanvasesUseCase: GetPagingCanvasesUseCase,
    private val archiveCanvasesUseCase: ArchiveCanvasesUseCase
) : ViewModel() {

    private val _canvasListUiState: MutableStateFlow<CanvasListUiState> =
        MutableStateFlow(CanvasListUiState())
    val canvasListUiState: StateFlow<CanvasListUiState> = _canvasListUiState.asStateFlow()

    private val eventChannel: Channel<CanvasListEvent> = Channel(Channel.BUFFERED)
    val eventsFlow: Flow<CanvasListEvent> = eventChannel.receiveAsFlow()

    private val refreshTrigger = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    @OptIn(ExperimentalCoroutinesApi::class)
    val basePagingFlow: Flow<PagingData<CanvasSummary>> =
        refreshTrigger
            .onStart { emit(Unit) }
            .flatMapLatest {
                canvasListUiState
                    .map { state ->
                        state.sortOption
                    }
                    .distinctUntilChanged()
                    .flatMapLatest { sortOption ->
                        getPagingCanvasesUseCase(
                            query = "",
                            sortOption = sortOption.name,
                            isRecycled = false
                        )
                    }
            }
            .cachedIn(viewModelScope)

//        refreshTrigger.onStart { emit(Unit) }
//            .flatMapLatest {
//                Pager(PagingConfig(pageSize = 1)) {
//                    object : PagingSource<Int, CanvasSummary>() {
//                        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CanvasSummary> {
//                            return LoadResult.Page(
//                                data = emptyList(),
//                                prevKey = null,
//                                nextKey = null
//                            )
//                        }
//
//                        override fun getRefreshKey(state: PagingState<Int, CanvasSummary>) = null
//                    }
//                }.flow
//            }


    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val searchPagingFlow: Flow<PagingData<CanvasSummary>> =
        canvasListUiState
            .map { state -> Triple(state.query.text, state.sortOption, false) }
            .distinctUntilChanged()
            .debounce(200)
            .flatMapLatest { (query, sortOption, isRecycled) ->
                if (query.isBlank()) {
                    flowOf(PagingData.empty())
                } else {
                    getPagingCanvasesUseCase(
                        query = query,
                        sortOption = sortOption.name,
                        isRecycled = isRecycled
                    ).cachedIn(viewModelScope)
                }
            }

    init {
        loadCanvasViewMode()

        _canvasListUiState.update { it.copy(isInitialLoading = true) }
        viewModelScope.launch {
            // Wait until first Paging load completes
            basePagingFlow.collectLatest {
                // When Paging emits data, hide initial loading
                Log.d("PAGING_DATA", basePagingFlow.toString())

                delay(2000)

                _canvasListUiState.update { state ->
                    state.copy(isInitialLoading = false, isRefreshing = false)
                }
            }
        }
    }

    private fun loadCanvasViewMode() {
        viewModelScope.launch {
            canvasRepository.getCanvasViewMode()
                .collectLatest { mode ->
                    _canvasListUiState.update { state ->
                        state.copy(canvasViewMode = mode)
                    }
                }
        }
    }


    fun viewAction(action: CanvasListAction) {
        when (action) {

            // Top Bar Actions
            is CanvasListAction.CloseDeleteBar -> {
                _canvasListUiState.update { state ->
                    state.copy(
                        topBarMode = TopBarMode.DEFAULT,
                        selectedIds = emptySet()
                    )
                }
            }

            is CanvasListAction.ToggleGridView -> {
                _canvasListUiState.update { state ->
                    state.copy(
                        canvasViewMode = if (state.canvasViewMode == CanvasViewMode.GRID)
                            CanvasViewMode.LIST
                        else
                            CanvasViewMode.GRID
                    )
                }

                viewModelScope.launch {
                    canvasRepository.setCanvasViewMode(_canvasListUiState.value.canvasViewMode)
                }
            }

            is CanvasListAction.RecycleBinClicked -> {
                viewModelScope.launch {
                    eventChannel.send(CanvasListEvent.NavigateToCanvasRecycle)
                }
            }

            is CanvasListAction.SortClicked -> {
                _canvasListUiState.update { state ->
                    state.copy(
                        topBarMode = TopBarMode.SORT,
                    )
                }
            }

            // Search Bar Actions
            is CanvasListAction.SearchMode -> {
                _canvasListUiState.update { state ->
                    state.copy(
                        topBarMode = TopBarMode.SEARCH,
                        selectedIds = emptySet(),
                        selectedScribbleId = -1
                    )
                }
            }

            is CanvasListAction.QueryChange -> {
                _canvasListUiState.update { state ->
                    state.copy(
                        query = action.query,
                        showEmptyScribbles = if (action.query.text.isBlank()) false else state.showEmptyScribbles
                    )
                }
            }

            is CanvasListAction.UpdateSortOption -> {
                _canvasListUiState.update { state ->
                    state.copy(
                        sortOption = action.sortOption,
                        selectedScribbleId = -1,
                        shouldScrollToTop = true,
                    )
                }
            }

            // Refresh Action
            is CanvasListAction.RefreshData -> {
                val state = _canvasListUiState.value
                if (state.isInitialLoading || state.topBarMode == TopBarMode.SEARCH) {
                    return
                }

                viewModelScope.launch {
                    _canvasListUiState.update { state ->
                        state.copy(
                            isInitialLoading = true,
                            isRefreshing = true
                        )
                    }

                    // Let Paging run a new query
                    refreshTrigger.emit(Unit)
                }
            }

            // List Actions
            is CanvasListAction.CanvasItemInteractionAction -> {
                val interaction = action.interaction
                when (interaction.clickType) {
                    CanvasItemClickType.CLICK -> {
                        val topBarMode = _canvasListUiState.value.topBarMode
                        if (topBarMode == TopBarMode.DELETE) {
                            toggleSelectedItems(id = interaction.selectedId)
                        } else {
                            if (_canvasListUiState.value.paneState == PaneState.TwoPane.Split) {
                                _canvasListUiState.update { state ->
                                    state.copy(
                                        selectedScribbleId = interaction.selectedId,
                                    )
                                }
                            } else {
                                viewModelScope.launch {
                                    eventChannel.send(
                                        CanvasListEvent.NavigateToCanvasDraw(
                                            CanvasDrawRoute(
                                                id = interaction.selectedId
                                            )
                                        )
                                    )
                                }
                            }
                        }
                    }

                    CanvasItemClickType.LONG_CLICK -> {
                        toggleSelectedItems(id = interaction.selectedId)
                    }
                }
            }

            // Floating Button Action
            is CanvasListAction.DrawCanvasClicked -> {
                viewModelScope.launch {
                    eventChannel.send(
                        CanvasListEvent.NavigateToCanvasDraw(
                            canvasDrawRoute = CanvasDrawRoute()
                        )
                    )
                }
            }

            // Update Actions
            is CanvasListAction.UpdateFabExpansion -> {
                _canvasListUiState.update { state ->
                    if (state.isFabExpanded == action.expanded) {
                        state
                    } else {
                        state.copy(isFabExpanded = action.expanded)
                    }
                }
            }

            is CanvasListAction.UpdateInitialLoading -> {
                _canvasListUiState.update { state ->
                    state.copy(
                        isInitialLoading = false
                    )
                }
            }

            is CanvasListAction.UpdateTopBarMode -> {
                _canvasListUiState.update { state ->
                    state.copy(
                        topBarMode = action.topBarMode
                    )
                }
            }

            is CanvasListAction.UpdateShowSearchEmpty -> {
                _canvasListUiState.update { state ->
                    state.copy(
                        showEmptyScribbles = action.showEmptyResultsText
                    )
                }
            }

            is CanvasListAction.Bulk -> {
                when (val event = action.event) {
                    is BulkActionEvent.Confirm -> {
                        val dialog = _canvasListUiState.value.dialogState

                        viewModelScope.launch {
                            val targetIds = dialog.targetIds

                            when (dialog.actionType) {
                                BulkRecycleActionType.RECYCLE_ALL -> {
                                    archiveCanvasesUseCase(
                                        targetIds,
                                        ArchiveAction.RECYCLE
                                    ).collectLatest { result: Result<ArchiveUseCaseResult, CanvasError> ->
                                        when (result) {
                                            is Result.Error -> Log.d(
                                                "CanvasArchive",
                                                result.error.toString()
                                            )

                                            is Result.Loading -> Log.d("CanvasArchive", "LOADING")
                                            is Result.Success -> {
                                                Log.d("CanvasArchive", "Result: ${result.data}")
                                                val selectedIds =
                                                    _canvasListUiState.value.selectedIds
                                                val selectedScribbleId =
                                                    _canvasListUiState.value.selectedScribbleId
                                                _canvasListUiState.update { state ->
                                                    state.copy(
                                                        topBarMode = TopBarMode.DEFAULT,
                                                        selectedIds = emptySet(),
                                                        selectedScribbleId = if (selectedScribbleId > 0 &&
                                                            selectedIds.contains(selectedScribbleId)
                                                        ) {
                                                            -1
                                                        } else {
                                                            selectedScribbleId
                                                        }
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }

                                else -> return@launch
                            }

                            _canvasListUiState.update {
                                it.copy(dialogState = ConfirmationDialogState())
                            }
                        }
                    }

                    is BulkActionEvent.Dismiss -> {
                        _canvasListUiState.update {
                            it.copy(dialogState = ConfirmationDialogState())
                        }
                    }

                    is BulkActionEvent.Request -> {
                        val (title, message) = when (event.type) {
                            BulkRecycleActionType.RECYCLE_ALL ->
                                "Move to Recycle Bin" to "Items will be archived."

                            else -> return
                        }

                        _canvasListUiState.update {
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

            is CanvasListAction.OnRevealChanged -> {
                val selectedScribbleId = _canvasListUiState.value.selectedScribbleId
                _canvasListUiState.update { state ->
                    state.copy(
                        paneReveal = action.reveal,
                        selectedScribbleId = if (action.reveal == 1f && selectedScribbleId > 0)
                            -1
                        else
                            selectedScribbleId,
                    )
                }
            }

            is CanvasListAction.ConsumedScrollToTop -> {
                _canvasListUiState.update { state ->
                    state.copy(
                        shouldScrollToTop = false
                    )
                }
            }

            is CanvasListAction.OnPageChanged -> {
                _canvasListUiState.update { state ->
                    state.copy(
                        selectedScribbleId = action.id
                    )
                }
            }

            is CanvasListAction.OnPaneChanged -> {
                val reveal = when (action.paneState) {
                    PaneState.SinglePane -> 1f
                    PaneState.TwoPane.FullList -> 1f
                    PaneState.TwoPane.Split -> 0.5f
                    PaneState.TwoPane.FullPreview -> 0f
                }

                _canvasListUiState.update { state ->
                    state.copy(
                        paneState = if (reveal < 0.5f && _canvasListUiState.value.selectedScribbleId < 0) PaneState.TwoPane.Split else action.paneState,
                        paneReveal = if (reveal < 0.5f && _canvasListUiState.value.selectedScribbleId < 0) 0.5f else reveal,
                        selectedScribbleId = if (action.paneState == PaneState.TwoPane.FullList || action.paneState == PaneState.SinglePane) {
                            -1
                        } else {
                            _canvasListUiState.value.selectedScribbleId
                        }
                    )
                }

                Log.d("TWO_PANE", "Local Reveal: ${_canvasListUiState.value.paneReveal}, PaneState: ${_canvasListUiState.value.paneState}")
            }
        }
    }

    private fun toggleSelectedItems(id: Long) {
        _canvasListUiState.update { state ->
            val newSelectedIds = if (state.selectedIds.contains(id)) {
                state.selectedIds - id
            } else {
                state.selectedIds + id
            }

            state.copy(
                selectedIds = newSelectedIds,
                topBarMode = if (newSelectedIds.isNotEmpty()) TopBarMode.DELETE else TopBarMode.DEFAULT
            )
        }
    }
}
package com.scribble.it.feature_canvas.presentation.canvaslist.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
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
import com.scribble.it.feature_canvas.presentation.canvaslist.state.PaneMode
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
import kotlinx.coroutines.flow.first
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

    private val sortFlow = canvasListUiState
        .map { state ->
            state.topBar.sortOption
        }
        .distinctUntilChanged()

    @OptIn(ExperimentalCoroutinesApi::class)
    val basePagingFlow: Flow<PagingData<CanvasSummary>> =
        refreshTrigger
            .onStart { emit(Unit) }
            .flatMapLatest {
                sortFlow
                    .flatMapLatest { sortOption ->
                        Log.d("PAGING_DATA", sortOption.toString())
                        getPagingCanvasesUseCase(
                            query = "",
                            sortOption = sortOption.name,
                            isRecycled = false
                        )
                    }
            }
            .cachedIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val searchPagingFlow: Flow<PagingData<CanvasSummary>> =
        canvasListUiState
            .map { state -> Triple(state.topBar.query.text, state.topBar.sortOption, false) }
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
                    )
                }
            }
            .cachedIn(viewModelScope)

    init {
        loadScribbleViewMode()
    }

    private fun loadScribbleViewMode() {
        viewModelScope.launch {

            val viewMode = canvasRepository.getScribbleViewMode().first()
            val sortMode = canvasRepository.getScribbleSortMode().first()

            _canvasListUiState.update { state ->
                state.copy(
                    topBar = state.topBar.copy(sortOption = sortMode),
                    list = state.list.copy(isInitialLoading = true, canvasViewMode = viewMode)
                )
            }
        }
    }

    fun viewAction(action: CanvasListAction) {
        when (action) {

            // Top Bar Actions
            is CanvasListAction.CloseDeleteBar -> {
                _canvasListUiState.update { state ->
                    state.copy(
                        topBar = state.topBar.copy(topBarMode = TopBarMode.DEFAULT),
                        list = state.list.copy(selectedIds = emptySet())
                    )
                }
            }

            is CanvasListAction.ToggleGridView -> {
                _canvasListUiState.update { state ->
                    state.copy(
                        list = state.list.copy(
                            canvasViewMode = if (state.list.canvasViewMode == CanvasViewMode.GRID)
                                CanvasViewMode.LIST
                            else
                                CanvasViewMode.GRID
                        )
                    )
                }

                viewModelScope.launch {
                    canvasRepository.setScribbleViewMode(_canvasListUiState.value.list.canvasViewMode)
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
                        topBar = state.topBar.copy(topBarMode = TopBarMode.SORT)
                    )
                }
            }

            // Search Bar Actions
            is CanvasListAction.SearchMode -> {
                _canvasListUiState.update { state ->
                    state.copy(
                        topBar = state.topBar.copy(topBarMode = TopBarMode.SEARCH),
                        list = state.list.copy(selectedIds = emptySet()),
                        pane = state.pane.copy(selectedScribbleId = -1)
                    )
                }
            }

            is CanvasListAction.QueryChange -> {
                _canvasListUiState.update { state ->
                    state.copy(
                        topBar = state.topBar.copy(query = action.query),
                        list = state.list.copy(
                            showEmptyScribbles = if (action.query.text.isBlank()) false else state.list.showEmptyScribbles
                        )
                    )
                }
            }

            is CanvasListAction.UpdateSortOption -> {
                _canvasListUiState.update { state ->
                    state.copy(
                        topBar = state.topBar.copy(sortOption = action.sortOption),
                        pane = state.pane.copy(selectedScribbleId = -1),
                        list = state.list.copy(shouldScrollToTop = true)
                    )
                }

                viewModelScope.launch {
                    canvasRepository.setScribbleSortMode(_canvasListUiState.value.topBar.sortOption)
                }
            }

            // Refresh Action
            is CanvasListAction.RefreshData -> {
                val state = _canvasListUiState.value
                if (state.list.isInitialLoading || state.topBar.topBarMode == TopBarMode.SEARCH) {
                    return
                }

                viewModelScope.launch {
                    _canvasListUiState.update { state ->
                        state.copy(
                            list = state.list.copy(
                                isInitialLoading = true,
                                isRefreshing = true
                            )
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
                        val topBarMode = _canvasListUiState.value.topBar.topBarMode
                        if (topBarMode == TopBarMode.DELETE) {
                            toggleSelectedItems(id = interaction.selectedId)
                        } else {
                            if (_canvasListUiState.value.pane.paneMode == PaneMode.TwoPane.Split) {
                                _canvasListUiState.update { state ->
                                    state.copy(
                                        pane = state.pane.copy(selectedScribbleId = interaction.selectedId)
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
                    if (state.fab.isFabExpanded == action.expanded) {
                        state
                    } else {
                        state.copy(fab = state.fab.copy(isFabExpanded = action.expanded))
                    }
                }
            }

            is CanvasListAction.UpdateInitialLoading -> {
                _canvasListUiState.update { state ->
                    state.copy(list = state.list.copy(isInitialLoading = false))
                }
            }

            is CanvasListAction.UpdateTopBarMode -> {
                _canvasListUiState.update { state ->
                    state.copy(topBar = state.topBar.copy(topBarMode = action.topBarMode))
                }
            }

            is CanvasListAction.UpdateShowSearchEmpty -> {
                _canvasListUiState.update { state ->
                    state.copy(list = state.list.copy(showEmptyScribbles = action.showEmptyResultsText))
                }
            }

            is CanvasListAction.Bulk -> {
                when (val event = action.event) {
                    is BulkActionEvent.Confirm -> {
                        val dialog = _canvasListUiState.value.dialog.confirmationDialog

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
                                                    _canvasListUiState.value.list.selectedIds
                                                val selectedScribbleId =
                                                    _canvasListUiState.value.pane.selectedScribbleId
                                                _canvasListUiState.update { state ->
                                                    state.copy(
                                                        topBar = state.topBar.copy(topBarMode = TopBarMode.DEFAULT),
                                                        list = state.list.copy(selectedIds = emptySet()),
                                                        pane = state.pane.copy(
                                                            selectedScribbleId = if (selectedScribbleId > 0 &&
                                                                selectedIds.contains(
                                                                    selectedScribbleId
                                                                )
                                                            ) {
                                                                -1
                                                            } else {
                                                                selectedScribbleId
                                                            }
                                                        )
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }

                                else -> return@launch
                            }

                            _canvasListUiState.update { state ->
                                state.copy(dialog = state.dialog.copy(confirmationDialog = ConfirmationDialogState()))
                            }
                        }
                    }

                    is BulkActionEvent.Dismiss -> {
                        _canvasListUiState.update { state ->
                            state.copy(dialog = state.dialog.copy(confirmationDialog = ConfirmationDialogState()))
                        }
                    }

                    is BulkActionEvent.Request -> {
                        val (title, message) = when (event.type) {
                            BulkRecycleActionType.RECYCLE_ALL ->
                                "Move to Recycle Bin" to "Items will be archived."

                            else -> return
                        }

                        _canvasListUiState.update { state ->
                            state.copy(
                                dialog = state.dialog.copy(
                                    confirmationDialog = ConfirmationDialogState(
                                        isVisible = true,
                                        title = title,
                                        message = message,
                                        actionType = event.type,
                                        targetIds = event.targetIds
                                    )
                                )
                            )
                        }
                    }
                }
            }

            is CanvasListAction.OnRevealChanged -> {
                val selectedScribbleId = _canvasListUiState.value.pane.selectedScribbleId
                _canvasListUiState.update { state ->
                    state.copy(
                        pane = state.pane.copy(
                            paneReveal = action.reveal,
                            selectedScribbleId = if (action.reveal == 1f && selectedScribbleId > 0)
                                -1
                            else
                                selectedScribbleId,
                        ),
                    )
                }
            }

            is CanvasListAction.ConsumedScrollToTop -> {
                _canvasListUiState.update { state ->
                    state.copy(
                        list = state.list.copy(shouldScrollToTop = false)
                    )
                }
            }

            is CanvasListAction.OnPageChanged -> {
                _canvasListUiState.update { state ->
                    state.copy(
                        pane = state.pane.copy(selectedScribbleId = action.id)
                    )
                }
            }

            is CanvasListAction.OnPaneChanged -> {
                val reveal = when (action.paneMode) {
                    PaneMode.SinglePane -> 1f
                    PaneMode.TwoPane.FullList -> 1f
                    PaneMode.TwoPane.Split -> 0.5f
                    PaneMode.TwoPane.FullPreview -> 0f
                }

                _canvasListUiState.update { state ->
                    val currentSelectedId = state.pane.selectedScribbleId
                    val newPaneState =
                        if (reveal < 0.5f && currentSelectedId < 0) PaneMode.TwoPane.Split else action.paneMode
                    val newReveal = if (reveal < 0.5f && currentSelectedId < 0) 0.5f else reveal
                    val newSelectedId =
                        if (action.paneMode == PaneMode.TwoPane.FullList || action.paneMode == PaneMode.SinglePane) {
                            -1
                        } else {
                            currentSelectedId
                        }

                    state.copy(
                        pane = state.pane.copy(
                            paneMode = newPaneState,
                            paneReveal = newReveal,
                            selectedScribbleId = newSelectedId
                        )
                    )
                }

                Log.d(
                    "TWO_PANE",
                    "Local Reveal: ${_canvasListUiState.value.pane.paneReveal}, PaneState: ${_canvasListUiState.value.pane.paneMode}"
                )
            }

            CanvasListAction.ClosePreview -> {
                _canvasListUiState.update { state ->
                    state.copy(
                        pane = state.pane.copy(
                            selectedScribbleId = -1
                        )
                    )
                }
            }

            CanvasListAction.EditPreview -> {
                val selectedScribbleId = _canvasListUiState.value.pane.selectedScribbleId
                viewModelScope.launch {
                    eventChannel.send(
                        CanvasListEvent.NavigateToCanvasDraw(
                            CanvasDrawRoute(
                                id = selectedScribbleId
                            )
                        )
                    )
                }
            }

            is CanvasListAction.OnLoadingCompleted -> {
                viewModelScope.launch {
                    delay(2000)
                    _canvasListUiState.update { state ->
                        state.copy(
                            list = state.list.copy(
                                isInitialLoading = false,
                                isRefreshing = false
                            )
                        )
                    }
                }
            }
        }
    }

    private fun toggleSelectedItems(id: Long) {
        _canvasListUiState.update { state ->
            val newSelectedIds = if (state.list.selectedIds.contains(id)) {
                state.list.selectedIds - id
            } else {
                state.list.selectedIds + id
            }

            state.copy(
                list = state.list.copy(selectedIds = newSelectedIds),
                topBar = state.topBar.copy(topBarMode = if (newSelectedIds.isNotEmpty()) TopBarMode.DELETE else TopBarMode.DEFAULT)
            )
        }
    }
}
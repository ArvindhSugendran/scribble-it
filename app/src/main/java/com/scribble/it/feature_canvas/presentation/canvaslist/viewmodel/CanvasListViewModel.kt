package com.scribble.it.feature_canvas.presentation.canvaslist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scribble.it.R
import com.scribble.it.feature_canvas.presentation.canvaslist.action.CanvasListAction
import com.scribble.it.feature_canvas.presentation.canvaslist.event.CanvasListEvent
import com.scribble.it.feature_canvas.presentation.canvaslist.state.CanvasListUiState
import com.scribble.it.feature_canvas.presentation.canvaslist.state.TopBarMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class CanvasTestingSummary(
    val id: Int? = null,
    val title: String,
    val thumbnailPath: Int? = null,
    val createdDate: Long,
    val modifiedDate: Long,
    val deletedAt: Long?
)

@HiltViewModel
class CanvasListViewModel @Inject constructor(
) : ViewModel() {

    private val sampleCanvases = listOf(
        CanvasTestingSummary(
            id = 1,
            title = "Wireframe Ideas",
            thumbnailPath = R.drawable.abstract_mountain,
            createdDate = 1704000000000,
            modifiedDate = 1704003600000,
            deletedAt = null
        ),
        CanvasTestingSummary(
            id = 2,
            title = "Daily Sketch",
            thumbnailPath = R.drawable.reddeadyellow,
            createdDate = 1703913600000,
            modifiedDate = 1703917200000,
            deletedAt = null
        ),
        CanvasTestingSummary(
            id = 3,
            title = "Flow Diagram",
            thumbnailPath = R.drawable.maan,
            createdDate = 1703827200000,
            modifiedDate = 1703830800000,
            deletedAt = null
        ),
        CanvasTestingSummary(
            id = 4,
            title = "App Architecture Notes",
            thumbnailPath = R.drawable.mountain,
            createdDate = 1703740800000,
            modifiedDate = 1703744400000,
            deletedAt = null
        ),
        CanvasTestingSummary(
            id = 5,
            title = "Landing Page Concepts",
            thumbnailPath = R.drawable.mountain_ranges,
            createdDate = 1703600000000,
            modifiedDate = 1703603600000,
            deletedAt = null
        ),
        CanvasTestingSummary(
            id = 6,
            title = "Experimental Color Study",
            thumbnailPath = R.drawable.pinkish,
            createdDate = 1703500000000,
            modifiedDate = 1703507200000,
            deletedAt = 1703600000000
        ),
        CanvasTestingSummary(
            id = 7,
            title = "Discarded UI Draft",
            thumbnailPath = R.drawable.red,
            createdDate = 1703400000000,
            modifiedDate = 1703403600000,
            deletedAt = 1703480000000
        ),
        CanvasTestingSummary(
            id = 8,
            title = "Background Illustration Test",
            thumbnailPath = R.drawable.reddeadblue,
            createdDate = 1703300000000,
            modifiedDate = 1703307200000,
            deletedAt = null
        ),
        CanvasTestingSummary(
            id = 9,
            title = "Sunrise Mood Board",
            thumbnailPath = R.drawable.sunrise,
            createdDate = 1703200000000,
            modifiedDate = 1703203600000,
            deletedAt = null
        ),
        CanvasTestingSummary(
            id = 10,
            title = "Sunset Color Palette",
            thumbnailPath = R.drawable.sunset,
            createdDate = 1703100000000,
            modifiedDate = 1703105400000,
            deletedAt = 1703200000000
        ),
        CanvasTestingSummary(
            id = 11,
            title = "Wireframe Ideas",
            thumbnailPath = R.drawable.abstract_mountain,
            createdDate = 1704000000000,
            modifiedDate = 1704003600000,
            deletedAt = null
        ),
        CanvasTestingSummary(
            id = 12,
            title = "Daily Sketch",
            thumbnailPath = R.drawable.reddeadyellow,
            createdDate = 1703913600000,
            modifiedDate = 1703917200000,
            deletedAt = null
        ),
        CanvasTestingSummary(
            id = 13,
            title = "Flow Diagram",
            thumbnailPath = R.drawable.maan,
            createdDate = 1703827200000,
            modifiedDate = 1703830800000,
            deletedAt = null
        ),
        CanvasTestingSummary(
            id = 14,
            title = "App Architecture Notes",
            thumbnailPath = R.drawable.mountain,
            createdDate = 1703740800000,
            modifiedDate = 1703744400000,
            deletedAt = null
        ),
        CanvasTestingSummary(
            id = 15,
            title = "Landing Page Concepts",
            thumbnailPath = R.drawable.mountain_ranges,
            createdDate = 1703600000000,
            modifiedDate = 1703603600000,
            deletedAt = null
        ),
        CanvasTestingSummary(
            id = 16,
            title = "Experimental Color Study",
            thumbnailPath = R.drawable.pinkish,
            createdDate = 1703500000000,
            modifiedDate = 1703507200000,
            deletedAt = 1703600000000
        ),
        CanvasTestingSummary(
            id = 17,
            title = "Discarded UI Draft",
            thumbnailPath = R.drawable.red,
            createdDate = 1703400000000,
            modifiedDate = 1703403600000,
            deletedAt = 1703480000000
        ),
        CanvasTestingSummary(
            id = 18,
            title = "Background Illustration Test",
            thumbnailPath = R.drawable.reddeadblue,
            createdDate = 1703300000000,
            modifiedDate = 1703307200000,
            deletedAt = null
        ),
        CanvasTestingSummary(
            id = 19,
            title = "Sunrise Mood Board",
            thumbnailPath = R.drawable.sunrise,
            createdDate = 1703200000000,
            modifiedDate = 1703203600000,
            deletedAt = null
        ),
        CanvasTestingSummary(
            id = 20,
            title = "Sunset Color Palette",
            thumbnailPath = R.drawable.sunset,
            createdDate = 1703100000000,
            modifiedDate = 1703105400000,
            deletedAt = 1703200000000
        )
    )

    private val _canvasListUiState: MutableStateFlow<CanvasListUiState> = MutableStateFlow(
        CanvasListUiState(
            canvasList = sampleCanvases
        )
    )
    val canvasListUiState: StateFlow<CanvasListUiState> = _canvasListUiState.asStateFlow()

    private val eventChannel: Channel<CanvasListEvent> = Channel(Channel.BUFFERED)
    val eventsFlow: Flow<CanvasListEvent> = eventChannel.receiveAsFlow()

    init {
        observeSearch()
    }

    @OptIn(FlowPreview::class)
    private fun observeSearch() {
        viewModelScope.launch {
            canvasListUiState
                .map { it: CanvasListUiState -> it.query }
                .debounce(150)
                .distinctUntilChanged()
                .collectLatest { query ->
                    val results = withContext(Dispatchers.Default) {
                        if (query.text.isBlank()) emptyList()
                        else _canvasListUiState.value.canvasList.filterCanvases(query.text)
                    }

                    _canvasListUiState.update { state ->
                        state.copy(
                            searchResults = results
                        )
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
                        isGrid = !state.isGrid
                    )
                }
            }

            is CanvasListAction.RecycleBinClicked -> TODO()
            is CanvasListAction.SortClicked -> TODO()

            // Search Bar Actions
            is CanvasListAction.FocusChange -> {
                _canvasListUiState.update { state ->
                    state.copy(
                        topBarMode = TopBarMode.SEARCH,
                        selectedIds = emptySet()
                    )
                }
            }

            is CanvasListAction.QueryChange -> {
                _canvasListUiState.update { state ->
                    state.copy(
                        query = action.query
                    )
                }
            }

            is CanvasListAction.SearchImeAction -> {
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
                    withContext(Dispatchers.IO) {
                        delay(6000)
                    }
                    _canvasListUiState.update { state ->
                        state.copy(
                            isInitialLoading = false,
                            isRefreshing = false
                        )
                    }
                }
            }

            // List Actions
            is CanvasListAction.CanvasItemClicked -> {
                toggleSelectedItems(id = action.id)
            }

            is CanvasListAction.CanvasItemLongClicked -> {
                toggleSelectedItems(id = action.id)
            }

            // Floating Button Action
            is CanvasListAction.DrawCanvasClicked -> TODO()

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

        }
    }

    private fun toggleSelectedItems(id: Int) {
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

    private fun List<CanvasTestingSummary>.filterCanvases(
        query: String
    ): List<CanvasTestingSummary> {
        val q = query.trim().lowercase()

        return this.filter { canvas ->
            canvas.title.lowercase().contains(q)
        }
    }

}
package com.scribble.it.feature_canvas.presentation.canvaslist.state

import com.scribble.it.feature_canvas.presentation.common.state.CanvasViewMode

data class ListUiState(
    val canvasViewMode: CanvasViewMode = CanvasViewMode.GRID,
    val selectedIds: Set<Long> = emptySet(),
    val isInitialLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val showEmptyScribbles: Boolean = false,
    val shouldScrollToTop: Boolean = false
)

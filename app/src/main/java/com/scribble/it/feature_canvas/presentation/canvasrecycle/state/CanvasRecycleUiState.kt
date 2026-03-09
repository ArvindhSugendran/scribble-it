package com.scribble.it.feature_canvas.presentation.canvasrecycle.state

import com.scribble.it.feature_canvas.presentation.common.dialog.state.ConfirmationDialogState
import com.scribble.it.feature_canvas.presentation.common.state.CanvasViewMode
import com.scribble.it.feature_canvas.presentation.common.state.TopBarMode

data class CanvasRecycleUiState (
    val topBarMode: TopBarMode = TopBarMode.DEFAULT,
    val canvasViewMode: CanvasViewMode = CanvasViewMode.GRID,
    val selectedIds: Set<Long> = emptySet(),
    val isAllSelected: Boolean = false,
    val isInitialLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val isGrid: Boolean = true,
    val showEmptyScribbles: Boolean = false,
    val dialogState: ConfirmationDialogState = ConfirmationDialogState()
)

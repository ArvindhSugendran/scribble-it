package com.scribble.it.feature_canvas.presentation.canvaslist.state

import androidx.compose.ui.text.input.TextFieldValue
import com.scribble.it.feature_canvas.domain.model.operation.SortOption
import com.scribble.it.feature_canvas.presentation.common.dialog.state.ConfirmationDialogState
import com.scribble.it.feature_canvas.presentation.common.state.TopBarMode

data class CanvasListUiState(
    val paneState: PaneState = PaneState.SinglePane,
    val topBarMode: TopBarMode = TopBarMode.DEFAULT,
    val selectedScribbleId: Long = -1,
    val selectedIds: Set<Long> = emptySet(),
    val isInitialLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val isFabExpanded: Boolean = true,
    val isGrid: Boolean = true,
    val showEmptyScribbles: Boolean = false,
    val shouldScrollToTop: Boolean = false,
    val paneReveal: Float = 0.5f,
    val query: TextFieldValue = TextFieldValue(""),
    val sortOption: SortOption = SortOption.CREATED_DATE_DESC,
    val sortOptions: List<SortOption> = SortOption.entries.toList(),
    val dialogState: ConfirmationDialogState = ConfirmationDialogState()
)
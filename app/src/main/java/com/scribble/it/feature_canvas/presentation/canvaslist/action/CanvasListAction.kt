package com.scribble.it.feature_canvas.presentation.canvaslist.action

import androidx.compose.ui.text.input.TextFieldValue
import com.scribble.it.feature_canvas.presentation.canvaslist.state.TopBarMode

sealed class CanvasListAction {
    // Top Bar Actions
    data object CloseDeleteBar: CanvasListAction()
    data object ToggleGridView: CanvasListAction()
    data object SortClicked: CanvasListAction()
    data object RecycleBinClicked: CanvasListAction()

    // Search Bar Actions
    data class QueryChange(val query: TextFieldValue): CanvasListAction()
    data object SearchImeAction: CanvasListAction()
    data object FocusChange: CanvasListAction()

    // Refresh Action
    data object RefreshData: CanvasListAction()

    // List Actions
    data class CanvasItemClicked(val id: Int): CanvasListAction()
    data class CanvasItemLongClicked(val id: Int): CanvasListAction()

    // Floating Button Action
    data object DrawCanvasClicked: CanvasListAction()

    // Update Actions
    data class UpdateFabExpansion(val expanded: Boolean) : CanvasListAction()
    data object UpdateInitialLoading: CanvasListAction()
    data class UpdateTopBarMode(val topBarMode: TopBarMode): CanvasListAction()
}
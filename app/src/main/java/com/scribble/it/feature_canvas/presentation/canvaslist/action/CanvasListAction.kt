package com.scribble.it.feature_canvas.presentation.canvaslist.action

import androidx.compose.ui.text.input.TextFieldValue
import com.scribble.it.feature_canvas.domain.model.operation.SortOption
import com.scribble.it.feature_canvas.presentation.canvaslist.state.PaneMode
import com.scribble.it.feature_canvas.presentation.common.action.BulkActionEvent
import com.scribble.it.feature_canvas.presentation.common.action.CanvasItemInteraction
import com.scribble.it.feature_canvas.presentation.common.state.TopBarMode

sealed class CanvasListAction {
    // Top Bar Actions
    data object CloseDeleteBar: CanvasListAction()
    data object ToggleGridView: CanvasListAction()
    data object SortClicked: CanvasListAction()
    data object RecycleBinClicked: CanvasListAction()

    // Search Bar Actions
    data class QueryChange(val query: TextFieldValue): CanvasListAction()
    data object SearchMode: CanvasListAction()

    // Refresh Action
    data object RefreshData: CanvasListAction()
    data object OnLoadingCompleted: CanvasListAction()

    // List Actions
    data class CanvasItemInteractionAction(val interaction: CanvasItemInteraction): CanvasListAction()

    // Floating Button Action
    data object DrawCanvasClicked: CanvasListAction()

    // Update Actions
    data object UpdateInitialLoading: CanvasListAction()
    data class UpdateFabExpansion(val expanded: Boolean) : CanvasListAction()
    data class UpdateTopBarMode(val topBarMode: TopBarMode): CanvasListAction()
    data class UpdateSortOption(val sortOption: SortOption): CanvasListAction()
    data class UpdateShowSearchEmpty(val showEmptyResultsText: Boolean): CanvasListAction()

    // Delete Action
    data class Bulk(val event: BulkActionEvent): CanvasListAction()

    // Drag Handle Action
    data class OnRevealChanged(val reveal: Float) : CanvasListAction()

    // Sort Action
    data object ConsumedScrollToTop: CanvasListAction()

    // Pager Action
    data class OnPageChanged(val id: Long): CanvasListAction()

    //Layout mode Action
    data class OnPaneChanged(val paneMode: PaneMode): CanvasListAction()

    // Preview Action
    data object ClosePreview: CanvasListAction()
    data object EditPreview: CanvasListAction()
}
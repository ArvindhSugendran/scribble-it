package com.scribble.it.feature_canvas.presentation.canvasrecycle.action

import com.scribble.it.feature_canvas.presentation.common.action.BulkActionEvent
import com.scribble.it.feature_canvas.presentation.common.action.CanvasItemInteraction

sealed class CanvasRecycleAction {
    data object CloseDeleteBar: CanvasRecycleAction()
    data object RefreshData: CanvasRecycleAction()

    data class CanvasItemInteractionAction(val interaction: CanvasItemInteraction): CanvasRecycleAction()
    data class SelectAllCanvases(val targetIds: Set<Long>): CanvasRecycleAction()

    data class Bulk(val event: BulkActionEvent): CanvasRecycleAction()
}
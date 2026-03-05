package com.scribble.it.feature_canvas.presentation.canvaslist.event

import com.scribble.it.feature_canvas.presentation.canvasdraw.navigation.CanvasDrawRoute

sealed class CanvasListEvent {
    data class NavigateToCanvasDraw(val canvasDrawRoute: CanvasDrawRoute): CanvasListEvent()
    data object NavigateToCanvasRecycle: CanvasListEvent()
}
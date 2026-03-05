package com.scribble.it.feature_canvas.presentation.canvasdraw.event

sealed class CanvasDrawEvent {
    data object NavigateBack : CanvasDrawEvent()
}
package com.scribble.it.feature_canvas.domain.model.operation

import com.scribble.it.feature_canvas.domain.model.canvas.CanvasDrawing

sealed interface DeleteRequest {
    data class Canvases(val canvases: List<CanvasDrawing>) : DeleteRequest
    data class OldRecycledCanvases(val timeStampLimit: Long) : DeleteRequest
}
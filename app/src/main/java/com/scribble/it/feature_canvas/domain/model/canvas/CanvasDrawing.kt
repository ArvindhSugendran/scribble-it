package com.scribble.it.feature_canvas.domain.model.canvas

import com.scribble.it.feature_canvas.domain.model.stroke.CanvasStroke

data class CanvasDrawing(
    val id: Int? = null,
    val title: String,
    val canvasStrokes: List<CanvasStroke>,
    val createdDate: Long,
    val modifiedDate: Long,
    val deletedAt: Long? = null
)
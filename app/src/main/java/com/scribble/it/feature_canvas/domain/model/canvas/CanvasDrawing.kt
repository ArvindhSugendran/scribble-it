package com.scribble.it.feature_canvas.domain.model.canvas

import com.scribble.it.feature_canvas.domain.model.stroke.CanvasStroke

data class CanvasDrawing(
    val id: Int? = null,
    val title: String,
    val canvasStrokes: List<CanvasStroke>,
    val thumbnailPath: String? = null,
    val createdDate: Long = 0L,
    val modifiedDate: Long = 0L,
    val deletedAt: Long? = null
)
package com.scribble.it.feature_canvas.domain.model.canvas

import com.scribble.it.feature_canvas.domain.model.stroke.CanvasStroke
import com.scribble.it.feature_canvas.presentation.canvasdraw.state.PageFormat

data class CanvasDrawing(
    val id: Long? = null,
    val title: String = "",
    val canvasStrokes: List<CanvasStroke> = emptyList(),
    val pageFormat: PageFormat = PageFormat.A4,
    val pageColor: Int = 0xFFFFFFFF.toInt(),
    val thumbnailPath: String? = null,
    val createdDate: Long = System.currentTimeMillis(),
    val modifiedDate: Long = System.currentTimeMillis(),
    val deletedDate: Long? = null
)
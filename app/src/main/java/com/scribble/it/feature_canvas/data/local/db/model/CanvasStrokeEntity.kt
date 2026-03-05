package com.scribble.it.feature_canvas.data.local.db.model

data class CanvasStrokeEntity(
    val x: Float,
    val y: Float,
    val penType: String,
    val brushSizePx: Float,
    val brushSizeNormalized: Float,
    val colorArgb: Int
)
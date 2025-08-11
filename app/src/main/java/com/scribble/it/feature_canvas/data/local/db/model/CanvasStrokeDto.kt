package com.scribble.it.feature_canvas.data.local.db.model

data class CanvasStrokeDto(
    val xCoordinate: Float,
    val yCoordinate: Float,
    val pen: String,
    val strokePX: Float,
    val color: Int
)
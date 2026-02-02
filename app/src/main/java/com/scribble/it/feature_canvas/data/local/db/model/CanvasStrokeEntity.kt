package com.scribble.it.feature_canvas.data.local.db.model

data class  CanvasStrokeEntity(
    val xCoordinate: Float,
    val yCoordinate: Float,
    val pen: String,
    val strokePx: Float,
    val color: Int
)
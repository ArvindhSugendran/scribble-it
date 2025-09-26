package com.scribble.it.feature_canvas.domain.model.stroke

data class CanvasStroke(
    val xCoordinate: Float,
    val yCoordinate: Float,
    val pen: PEN,
    val strokePx: Float,
    val color: Int
)
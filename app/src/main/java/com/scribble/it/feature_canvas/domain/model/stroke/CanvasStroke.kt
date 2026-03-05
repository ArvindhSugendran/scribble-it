package com.scribble.it.feature_canvas.domain.model.stroke

data class CanvasStroke(
    val x: Float,                     // X coordinate on the logical canvas (0 → PAGE_WIDTH)
    val y: Float,                     // Y coordinate on the logical canvas (0 → PAGE_HEIGHT)
    val penType: PEN,                 // Type of pen action: MOVE, DRAW, DOT
    val brushSizePx: Float,           // Actual brush size in pixels (for rendering)
    val brushSizeNormalized: Float,   // Brush size normalized relative to canvas width (0 → 1)
    val colorArgb: Int                // Color in ARGB format
)
package com.scribble.it.feature_canvas.presentation.canvasdraw.state

enum class BrushSize(
    val mm: Float
) {
    VERY_THIN (0.2f),
    THIN(0.35f),
    MEDIUM (0.5f),
    THICK(0.8f),
    MARKER(1.2f)
}
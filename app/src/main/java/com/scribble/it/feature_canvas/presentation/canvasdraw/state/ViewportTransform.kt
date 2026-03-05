package com.scribble.it.feature_canvas.presentation.canvasdraw.state

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.DpSize

data class ViewportTransform(
    val scale: Float,
    val offsetPx: Offset,
    val viewportSize: DpSize
)

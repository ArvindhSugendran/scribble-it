package com.scribble.it.feature_canvas.presentation.canvasdraw.ui.adaptive.metrics

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset

data class CanvasViewportMetrics(
    val viewportWidth: Dp,
    val viewportHeight: Dp,
    val maxScale: Float,
    val scale: Float,
    val offset: DpOffset
)

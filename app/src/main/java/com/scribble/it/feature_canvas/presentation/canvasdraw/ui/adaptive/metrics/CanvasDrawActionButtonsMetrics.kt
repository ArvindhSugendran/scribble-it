package com.scribble.it.feature_canvas.presentation.canvasdraw.ui.adaptive.metrics

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

data class CanvasDrawActionButtonsMetrics(
    val buttonSize: Dp = 50.dp,
    val innerShadowRadius: Dp = 5.dp,
    val innerShadowSpread: Dp = 2.dp,
    val innerShadowOffset: DpOffset = DpOffset(x = 3.dp, y = 4.dp),
    val allowScroll: Boolean = false
)

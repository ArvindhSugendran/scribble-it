package com.scribble.it.feature_canvas.presentation.canvaslist.ui.adaptive.environment

import androidx.compose.runtime.compositionLocalOf
import com.scribble.it.feature_canvas.presentation.canvaslist.ui.adaptive.metrics.canvasList.CanvasMetricsBundle

val LocalCanvasMetrics = compositionLocalOf<CanvasMetricsBundle> {
    error("CanvasMetrics not provided")
}
package com.scribble.it.feature_canvas.presentation.canvaslist.ui.adaptive.environment

import androidx.compose.runtime.compositionLocalOf
import com.scribble.it.feature_canvas.presentation.canvaslist.ui.adaptive.metrics.preview.PreviewScreenMetrics

val LocalPreviewCanvasMetrics = compositionLocalOf<PreviewScreenMetrics> {
    error("PreviewMetrics not provided")
}
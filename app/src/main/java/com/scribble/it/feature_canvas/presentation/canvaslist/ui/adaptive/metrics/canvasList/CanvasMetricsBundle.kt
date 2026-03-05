package com.scribble.it.feature_canvas.presentation.canvaslist.ui.adaptive.metrics.canvasList

import com.scribble.it.feature_canvas.presentation.common.ui.adaptive.metrics.CanvasAppBarMetrics
import com.scribble.it.feature_canvas.presentation.common.ui.adaptive.metrics.CanvasGridMetrics
import com.scribble.it.feature_canvas.presentation.common.ui.adaptive.metrics.CanvasItemMetrics

data class CanvasMetricsBundle(
    val screen: CanvasListScreenMetrics,
    val grid: CanvasGridMetrics,
    val item: CanvasItemMetrics,
    val appBar: CanvasAppBarMetrics,
    val floatingButton: CanvasFloatingButtonMetrics,
    val sortBar: CanvasSortBarMetrics
)

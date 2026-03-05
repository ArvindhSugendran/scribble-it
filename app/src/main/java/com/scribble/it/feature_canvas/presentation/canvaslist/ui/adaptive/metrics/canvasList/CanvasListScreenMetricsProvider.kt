package com.scribble.it.feature_canvas.presentation.canvaslist.ui.adaptive.metrics.canvasList

import androidx.compose.ui.unit.dp
import com.scribble.it.ui.adaptive.layoutConfig.LayoutConfiguration
import com.scribble.it.ui.adaptive.scale.ScreenScale
import com.scribble.it.ui.adaptive.scale.WidthClass

fun retrieveCanvasListScreenMetrics(
    scale: ScreenScale,
    layout: LayoutConfiguration
): CanvasListScreenMetrics {

    return when (layout.width) {
        WidthClass.COMPACT -> compactMetrics()
        WidthClass.MEDIUM -> mediumMetrics()
        WidthClass.EXPANDED -> expandedMetrics()
        WidthClass.LARGE,
        WidthClass.EXTRA_LARGE -> TODO()
    }
}

private fun compactMetrics(): CanvasListScreenMetrics {
    return CanvasListScreenMetrics(
        horizontalPadding = 0.dp
    )
}

private fun mediumMetrics(): CanvasListScreenMetrics {
    return CanvasListScreenMetrics(
        horizontalPadding = 16.dp
    )
}

private fun expandedMetrics(): CanvasListScreenMetrics {
    return CanvasListScreenMetrics(
        horizontalPadding = 16.dp
    )
}
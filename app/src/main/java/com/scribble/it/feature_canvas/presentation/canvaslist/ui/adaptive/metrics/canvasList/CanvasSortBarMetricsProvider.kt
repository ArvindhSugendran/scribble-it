package com.scribble.it.feature_canvas.presentation.canvaslist.ui.adaptive.metrics.canvasList

import com.scribble.it.ui.adaptive.layoutConfig.LayoutConfiguration
import com.scribble.it.ui.adaptive.scale.ScreenScale
import com.scribble.it.ui.adaptive.scale.WidthClass

fun retrieveCanvasSortBarMetrics(
    scale: ScreenScale,
    layout: LayoutConfiguration
): CanvasSortBarMetrics {
    return when (layout.width) {
        WidthClass.COMPACT -> compactMetrics(scale)
        WidthClass.MEDIUM -> mediumMetrics(scale)
        WidthClass.EXPANDED -> expandedMetrics(scale)
        WidthClass.LARGE,
        WidthClass.EXTRA_LARGE -> TODO()
    }
}

private fun compactMetrics(
    s: ScreenScale
): CanvasSortBarMetrics {
    return CanvasSortBarMetrics(
        allowScroll = true
    )
}

private fun mediumMetrics(
    s: ScreenScale
): CanvasSortBarMetrics {
    return CanvasSortBarMetrics(
        allowScroll = false
    )
}

private fun expandedMetrics(
    s: ScreenScale
): CanvasSortBarMetrics {
    return CanvasSortBarMetrics(
        allowScroll = false
    )
}
package com.scribble.it.feature_canvas.presentation.canvaslist.ui

import com.scribble.it.ui.adaptive.layoutConfig.LayoutConfiguration
import com.scribble.it.ui.adaptive.scale.ScreenScale
import com.scribble.it.ui.adaptive.scale.WidthClass

fun retrieveCanvasListScreenMetrics(
    scale: ScreenScale,
    layout: LayoutConfiguration
): CanvasScreenMetrics {

    return when (layout.width) {
        WidthClass.COMPACT -> compactMetrics()
        WidthClass.MEDIUM -> mediumMetrics()
        WidthClass.EXPANDED -> expandedMetrics()
        WidthClass.LARGE,
        WidthClass.EXTRA_LARGE -> largeMetrics()
    }
}

private fun compactMetrics(): CanvasScreenMetrics {
    return CanvasScreenMetrics(
        gridCellsCount = 2
    )
}

private fun mediumMetrics(): CanvasScreenMetrics {
    return CanvasScreenMetrics(
        gridCellsCount = 3
    )
}

private fun expandedMetrics(): CanvasScreenMetrics {
    return CanvasScreenMetrics(
        gridCellsCount = 4
    )
}

private fun largeMetrics(): CanvasScreenMetrics {
    return CanvasScreenMetrics(
        gridCellsCount = 5
    )
}
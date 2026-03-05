package com.scribble.it.feature_canvas.presentation.canvaslist.ui.adaptive.metrics.canvasList

import androidx.compose.ui.unit.dp
import com.scribble.it.ui.adaptive.layoutConfig.LayoutConfiguration
import com.scribble.it.ui.adaptive.scale.ScreenScale
import com.scribble.it.ui.adaptive.scale.WidthClass

fun retrieveCanvasFloatingButtonMetrics(
    scale: ScreenScale,
    layout: LayoutConfiguration
): CanvasFloatingButtonMetrics {
    return when (layout.width) {
        WidthClass.COMPACT -> compactMetrics(scale)
        WidthClass.MEDIUM -> mediumMetrics()
        WidthClass.EXPANDED -> expandedMetrics()
        WidthClass.LARGE,
        WidthClass.EXTRA_LARGE -> largeMetrics()
    }
}

private fun compactMetrics(
    s: ScreenScale
): CanvasFloatingButtonMetrics {
    return if (s.width < 250.dp)
        CanvasFloatingButtonMetrics(buttonText = "Draw")
    else
        CanvasFloatingButtonMetrics()
}

private fun mediumMetrics(): CanvasFloatingButtonMetrics {
    return CanvasFloatingButtonMetrics()
}

private fun expandedMetrics(): CanvasFloatingButtonMetrics {
    return CanvasFloatingButtonMetrics()
}

private fun largeMetrics(): CanvasFloatingButtonMetrics {
    return CanvasFloatingButtonMetrics()
}
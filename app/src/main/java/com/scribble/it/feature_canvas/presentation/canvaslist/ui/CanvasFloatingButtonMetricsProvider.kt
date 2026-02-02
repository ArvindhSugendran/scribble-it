package com.scribble.it.feature_canvas.presentation.canvaslist.ui

import androidx.compose.ui.unit.dp
import com.scribble.it.ui.adaptive.layoutConfig.LayoutConfiguration
import com.scribble.it.ui.adaptive.scale.ScreenScale
import com.scribble.it.ui.adaptive.scale.WidthClass

fun retrieveCanvasFloatingButtonMetrics(
    scale: ScreenScale,
    layout: LayoutConfiguration
): CanvasFloatingButtonMetrics {
    return when(layout.width) {
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

    val isVeryNarrow = s.width < 300.dp

    return if(isVeryNarrow)
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
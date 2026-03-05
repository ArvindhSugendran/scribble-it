package com.scribble.it.feature_canvas.presentation.canvasdraw.ui.adaptive.metrics

import com.scribble.it.ui.adaptive.layoutConfig.LayoutConfiguration
import com.scribble.it.ui.adaptive.scale.ScreenScale
import com.scribble.it.ui.adaptive.scale.WidthClass

fun retrieveCanvasDrawAppBarMetrics(
    scale: ScreenScale,
    layout: LayoutConfiguration
): CanvasDrawAppBarMetrics {
    return when(layout.width) {
        WidthClass.COMPACT -> compactMetrics(scale)
        WidthClass.MEDIUM -> mediumMetrics(scale)
        WidthClass.EXPANDED -> expandedMetrics(scale)
        WidthClass.LARGE,
        WidthClass.EXTRA_LARGE -> TODO()
    }
}

private fun compactMetrics(
    s: ScreenScale,
): CanvasDrawAppBarMetrics {

    return if(s.isVeryNarrowWidth) {
        CanvasDrawAppBarMetrics(
            iconsSpacedByPadding = s.w(0.025f),
            iconVisibility = false
        )
    } else {
        CanvasDrawAppBarMetrics(
            iconsSpacedByPadding = s.w(0.035f),
            iconVisibility = true
        )
    }
}

private fun mediumMetrics(
    s: ScreenScale,
): CanvasDrawAppBarMetrics {
    return CanvasDrawAppBarMetrics(
        iconsSpacedByPadding = s.w(0.03f),
        iconVisibility = true
    )
}

private fun expandedMetrics(
    s: ScreenScale,
): CanvasDrawAppBarMetrics {
    return CanvasDrawAppBarMetrics(
        iconsSpacedByPadding = s.w(0.03f),
        iconVisibility = true
    )
}
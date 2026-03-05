package com.scribble.it.feature_canvas.presentation.common.ui.adaptive.metrics

import androidx.compose.material3.Typography
import com.scribble.it.ui.adaptive.layoutConfig.LayoutConfiguration
import com.scribble.it.ui.adaptive.scale.ScreenScale
import com.scribble.it.ui.adaptive.scale.WidthClass

fun retrieveCanvasAppBarMetrics(
    scale: ScreenScale,
    layout: LayoutConfiguration,
    typography: Typography
): CanvasAppBarMetrics {
    return when(layout.width) {
        WidthClass.COMPACT -> compactMetrics(scale, typography)
        WidthClass.MEDIUM -> mediumMetrics(scale, typography)
        WidthClass.EXPANDED -> expandedMetrics(scale, typography)
        WidthClass.LARGE,
        WidthClass.EXTRA_LARGE -> TODO()
    }
}

private fun compactMetrics(
    s: ScreenScale,
    typography: Typography
): CanvasAppBarMetrics {

    return if(s.isVeryNarrowWidth) {
        CanvasAppBarMetrics(
            appNameTextStyle = typography.headlineMedium,
            iconsSpacedByPadding = s.w(0.025f)
        )
    } else {
        CanvasAppBarMetrics(
            appNameTextStyle = typography.headlineLarge,
            iconsSpacedByPadding = s.w(0.035f)
        )
    }
}

private fun mediumMetrics(
    s: ScreenScale,
    typography: Typography
): CanvasAppBarMetrics {
    return CanvasAppBarMetrics(
        appNameTextStyle = typography.headlineLarge,
        iconsSpacedByPadding = s.w(0.03f)
    )
}

private fun expandedMetrics(
    s: ScreenScale,
    typography: Typography
): CanvasAppBarMetrics {
    return CanvasAppBarMetrics(
        appNameTextStyle = typography.headlineLarge,
        iconsSpacedByPadding = s.w(0.03f)
    )
}
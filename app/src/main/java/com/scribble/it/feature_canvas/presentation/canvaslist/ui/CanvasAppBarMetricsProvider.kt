package com.scribble.it.feature_canvas.presentation.canvaslist.ui

import androidx.compose.material3.Typography
import androidx.compose.ui.unit.dp
import com.scribble.it.ui.adaptive.layoutConfig.LayoutConfiguration
import com.scribble.it.ui.adaptive.scale.ScreenScale
import com.scribble.it.ui.adaptive.scale.WidthClass

fun retrieveCanvasTopBarMetrics(
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

    val isVeryNarrow = s.width < 300.dp

    return if(isVeryNarrow) {
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
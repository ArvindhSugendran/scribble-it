package com.scribble.it.feature_canvas.presentation.common.ui.adaptive.metrics

import androidx.compose.material3.Typography
import com.scribble.it.ui.adaptive.layoutConfig.LayoutConfiguration
import com.scribble.it.ui.adaptive.scale.ScreenScale
import com.scribble.it.ui.adaptive.scale.WidthClass

fun retrieveCanvasItemMetrics(
    scale: ScreenScale,
    layout: LayoutConfiguration,
    typography: Typography
): CanvasItemMetrics {
    return when (layout.width) {
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
): CanvasItemMetrics {
    return CanvasItemMetrics(
        titleTextStyle = typography.bodyMedium,
        dateTextStyle = typography.bodySmall,
    )
}

private fun mediumMetrics(
    s: ScreenScale,
    typography: Typography
): CanvasItemMetrics {
    return CanvasItemMetrics(
        titleTextStyle = typography.bodyLarge,
        dateTextStyle = typography.bodyMedium,
    )
}


private fun expandedMetrics(
    s: ScreenScale,
    typography: Typography
): CanvasItemMetrics {
    return CanvasItemMetrics(
        titleTextStyle = typography.titleMedium,
        dateTextStyle = typography.bodyMedium,
    )
}



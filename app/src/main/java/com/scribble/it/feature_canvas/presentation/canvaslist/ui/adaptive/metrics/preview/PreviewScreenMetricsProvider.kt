package com.scribble.it.feature_canvas.presentation.canvaslist.ui.adaptive.metrics.preview

import androidx.compose.runtime.Composable
import com.scribble.it.ui.adaptive.layoutConfig.LayoutConfiguration
import com.scribble.it.ui.adaptive.scale.HeightClass
import com.scribble.it.ui.adaptive.scale.ScreenScale
import com.scribble.it.ui.adaptive.scale.WidthClass

@Composable
fun rememberPreviewScreenMetrics(
    scale: ScreenScale,
    layout: LayoutConfiguration
): PreviewScreenMetrics {
    return when (layout.width) {
        WidthClass.COMPACT -> compactMetrics(layout.height)
        WidthClass.MEDIUM -> mediumMetrics(layout.height)
        WidthClass.EXPANDED -> expandedMetrics(layout.height)
        WidthClass.LARGE,
        WidthClass.EXTRA_LARGE -> TODO()
    }
}

private fun compactMetrics(
    heightClass: HeightClass
): PreviewScreenMetrics {
    return when(heightClass) {
        HeightClass.COMPACT -> TODO()
        HeightClass.MEDIUM -> {
            PreviewScreenMetrics(
                infoCardStyleChange = false
            )
        }
        HeightClass.EXPANDED -> {
            PreviewScreenMetrics(
                infoCardStyleChange = false
            )
        }
    }
}

private fun mediumMetrics(
    heightClass: HeightClass
): PreviewScreenMetrics {
    return when(heightClass) {
        HeightClass.COMPACT -> TODO()
        HeightClass.MEDIUM -> {
            PreviewScreenMetrics(
                infoCardStyleChange = true
            )
        }
        HeightClass.EXPANDED -> {
            PreviewScreenMetrics(
                infoCardStyleChange = true
            )
        }
    }
}

private fun expandedMetrics(
    heightClass: HeightClass
): PreviewScreenMetrics {
    return when(heightClass) {
        HeightClass.COMPACT -> TODO()
        HeightClass.MEDIUM -> {
            PreviewScreenMetrics(
                infoCardStyleChange = true
            )
        }
        HeightClass.EXPANDED -> {
            PreviewScreenMetrics(
                infoCardStyleChange = true
            )
        }
    }
}
package com.scribble.it.feature_canvas.presentation.canvaslist.ui.adaptive.metrics.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.scribble.it.feature_canvas.presentation.canvasrecycle.ui.adaptive.metrics.CanvasRecycleScreenMetrics
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
        WidthClass.COMPACT -> compactMetrics(scale, layout.height)
        WidthClass.MEDIUM -> mediumMetrics(scale, layout.height)
        WidthClass.EXPANDED -> expandedMetrics(scale, layout.height)
        WidthClass.LARGE,
        WidthClass.EXTRA_LARGE -> TODO()
    }
}

private fun compactMetrics(
    s: ScreenScale,
    heightClass: HeightClass
): PreviewScreenMetrics {
    val previewScreenMetrics = PreviewScreenMetrics(
        infoCardStyleChange = false
    )

    return when (heightClass) {
        HeightClass.COMPACT -> TODO()
        HeightClass.MEDIUM -> {
            previewScreenMetrics.copy(
                illustrationSize = s.min(0.5f),
            )
        }

        HeightClass.EXPANDED -> {
            previewScreenMetrics.copy(
                illustrationSize = s.min(0.6f),
            )
        }
    }
}

private fun mediumMetrics(
    s: ScreenScale,
    heightClass: HeightClass
): PreviewScreenMetrics {
    val previewScreenMetrics = PreviewScreenMetrics(
        infoCardStyleChange = true
    )

    return when (heightClass) {
        HeightClass.COMPACT -> TODO()
        HeightClass.MEDIUM -> {
            previewScreenMetrics.copy(
                illustrationSize = s.min(0.5f),
            )
        }

        HeightClass.EXPANDED -> {
            previewScreenMetrics.copy(
                illustrationSize = s.min(0.6f),
            )
        }
    }
}

private fun expandedMetrics(
    s: ScreenScale,
    heightClass: HeightClass
): PreviewScreenMetrics {
    val previewScreenMetrics = PreviewScreenMetrics(
        infoCardStyleChange = true
    )

    return when (heightClass) {
        HeightClass.COMPACT -> TODO()
        HeightClass.MEDIUM -> {
            previewScreenMetrics.copy(
                illustrationSize = s.min(0.5f),
            )
        }

        HeightClass.EXPANDED -> {
            previewScreenMetrics.copy(
                illustrationSize = s.min(0.6f),
            )
        }
    }
}
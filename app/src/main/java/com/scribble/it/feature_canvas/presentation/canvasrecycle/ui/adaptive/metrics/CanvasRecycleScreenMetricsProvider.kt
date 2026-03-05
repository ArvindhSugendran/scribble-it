package com.scribble.it.feature_canvas.presentation.canvasrecycle.ui.adaptive.metrics

import androidx.compose.ui.unit.dp
import com.scribble.it.ui.adaptive.layoutConfig.LayoutConfiguration
import com.scribble.it.ui.adaptive.scale.HeightClass
import com.scribble.it.ui.adaptive.scale.ScreenScale
import com.scribble.it.ui.adaptive.scale.WidthClass

fun retrieveCanvasRecycleScreenMetrics(
    scale: ScreenScale,
    layout: LayoutConfiguration
): CanvasRecycleScreenMetrics {

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
): CanvasRecycleScreenMetrics {
    val canvasRecycleScreenMetrics = CanvasRecycleScreenMetrics(
        horizontalPadding = 0.dp
    )
    return when (heightClass) {
        HeightClass.COMPACT -> {
            canvasRecycleScreenMetrics.copy(
                illustrationSize = s.min(0.32f),
            )
        }

        HeightClass.MEDIUM -> {
            canvasRecycleScreenMetrics.copy(
                illustrationSize = s.min(0.5f),
            )
        }

        HeightClass.EXPANDED -> {
            canvasRecycleScreenMetrics.copy(
                illustrationSize = s.min(0.6f),
            )
        }
    }
}

private fun mediumMetrics(
    s: ScreenScale,
    heightClass: HeightClass
): CanvasRecycleScreenMetrics {
    val canvasRecycleScreenMetrics = CanvasRecycleScreenMetrics(
        horizontalPadding = 16.dp
    )
    return when (heightClass) {
        HeightClass.COMPACT -> {
            canvasRecycleScreenMetrics.copy(
                illustrationSize = s.min(0.32f),
            )
        }

        HeightClass.MEDIUM -> {
            canvasRecycleScreenMetrics.copy(
                illustrationSize = s.min(0.4f),
            )
        }

        HeightClass.EXPANDED -> {
            canvasRecycleScreenMetrics.copy(
                illustrationSize = s.min(0.6f),
            )
        }
    }
}

private fun expandedMetrics(
    s: ScreenScale,
    heightClass: HeightClass
): CanvasRecycleScreenMetrics {
    val canvasRecycleScreenMetrics = CanvasRecycleScreenMetrics(
        horizontalPadding = 16.dp
    )
    return when (heightClass) {
        HeightClass.COMPACT -> {
            canvasRecycleScreenMetrics.copy(
                illustrationSize = s.min(0.4f),
            )
        }

        HeightClass.MEDIUM -> {
            canvasRecycleScreenMetrics.copy(
                illustrationSize = s.min(0.5f),
            )
        }

        HeightClass.EXPANDED -> {
            canvasRecycleScreenMetrics.copy(
                illustrationSize = s.min(0.6f),
            )
        }
    }
}
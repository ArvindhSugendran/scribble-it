package com.scribble.it.feature_canvas.presentation.canvasdraw.ui.adaptive.metrics

import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.scribble.it.ui.adaptive.layoutConfig.LayoutConfiguration
import com.scribble.it.ui.adaptive.scale.HeightClass
import com.scribble.it.ui.adaptive.scale.ScreenScale
import com.scribble.it.ui.adaptive.scale.WidthClass

fun retrieveCanvasDrawActionButtonsMetrics(
    scale: ScreenScale,
    layout: LayoutConfiguration
): CanvasDrawActionButtonsMetrics {
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
    height: HeightClass
): CanvasDrawActionButtonsMetrics {

    return when (height) {
        HeightClass.COMPACT -> {
            compactMetrics(s.isVeryNarrowHeight)
        }

        HeightClass.MEDIUM -> {
            CanvasDrawActionButtonsMetrics(
                buttonSize = 50.dp
            )
        }

        HeightClass.EXPANDED -> {
            CanvasDrawActionButtonsMetrics(
                buttonSize = 50.dp
            )
        }
    }
}

private fun mediumMetrics(
    s: ScreenScale,
    height: HeightClass
): CanvasDrawActionButtonsMetrics {

    return when (height) {
        HeightClass.COMPACT -> {
            compactMetrics(s.isVeryNarrowHeight)
        }

        HeightClass.MEDIUM -> {
            CanvasDrawActionButtonsMetrics(
                buttonSize = 50.dp
            )
        }

        HeightClass.EXPANDED -> {
            CanvasDrawActionButtonsMetrics(
                buttonSize = 50.dp
            )
        }
    }
}

private fun expandedMetrics(
    s: ScreenScale,
    height: HeightClass
): CanvasDrawActionButtonsMetrics {
    return when (height) {
        HeightClass.COMPACT -> {
            compactMetrics(s.isVeryNarrowHeight)
        }

        HeightClass.MEDIUM -> {
            CanvasDrawActionButtonsMetrics(
                buttonSize = 50.dp
            )
        }

        HeightClass.EXPANDED -> {
            CanvasDrawActionButtonsMetrics(
                buttonSize = 50.dp
            )
        }
    }
}

private fun compactMetrics(
    isVeryNarrow: Boolean
): CanvasDrawActionButtonsMetrics {
    return if (isVeryNarrow) {
        CanvasDrawActionButtonsMetrics(
            buttonSize = 50.dp,
            innerShadowRadius = 5.dp,
            innerShadowSpread = 2.dp,
            innerShadowOffset = DpOffset(x = 3.dp, y = 4.dp),
            allowScroll = true
        )
    } else {
        CanvasDrawActionButtonsMetrics(
            buttonSize = 50.dp,
            innerShadowRadius = 5.dp,
            innerShadowSpread = 2.dp,
            innerShadowOffset = DpOffset(x = 3.dp, y = 4.dp),
        )
    }
}
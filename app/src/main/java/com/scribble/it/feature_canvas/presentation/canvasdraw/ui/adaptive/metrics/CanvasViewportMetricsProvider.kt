package com.scribble.it.feature_canvas.presentation.canvasdraw.ui.adaptive.metrics

import android.util.Log
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.scribble.it.feature_canvas.presentation.canvasdraw.state.PageFormat
import com.scribble.it.ui.adaptive.layoutConfig.LayoutConfiguration
import com.scribble.it.ui.adaptive.scale.HeightClass
import com.scribble.it.ui.adaptive.scale.ScreenScale
import com.scribble.it.ui.adaptive.scale.WidthClass

fun retrieveCanvasViewportMetrics(
    scale: ScreenScale,
    layout: LayoutConfiguration,
    pageFormat: PageFormat
): CanvasViewportMetrics {
    return computeViewportMetrics(scale, layout, pageFormat.aspectRatio)
}

private fun computeViewportMetrics(
    s: ScreenScale,
    layout: LayoutConfiguration,
    pageAspectRatio: Float
): CanvasViewportMetrics {

    val (viewportWidth, viewportHeight) = s.constrainedSize(pageAspectRatio)
    val maxScale = decideViewportScale(layout.width, layout.height)
    val scale = if (s.width < s.height) {
        1f
    } else {
        maxScale / 4f
    }

    val scaledWidth = scale * viewportWidth
    val scaledHeight = scale * viewportHeight

    val overFlowWidth = (scaledWidth - s.width).coerceAtLeast(0.dp)
    val overFlowHeight = (scaledHeight - s.height).coerceAtLeast(0.dp)

    val maxOverFlowWidth = overFlowWidth / 2f
    val maxOverFlowHeight = overFlowHeight / 2f

    val offset = if (scale == 1f) {
        DpOffset.Zero
    } else {
        DpOffset(
            x = maxOverFlowWidth,
            y = maxOverFlowHeight
        )
    }

    Log.d("LayoutConfig_", "scale = $scale : offset = $offset")

    return CanvasViewportMetrics(
        viewportWidth = viewportWidth,
        viewportHeight = viewportHeight,
        maxScale = maxScale,
        scale = scale,
        offset = offset
    )
}

private fun decideViewportScale(
    width: WidthClass,
    height: HeightClass,
): Float {
    return when (width) {
        WidthClass.COMPACT -> compactMetrics(height)
        WidthClass.MEDIUM -> mediumMetrics(height)
        WidthClass.EXPANDED -> expandedMetrics(height)
        WidthClass.LARGE,
        WidthClass.EXTRA_LARGE -> TODO()
    }
}

private fun compactMetrics(
    height: HeightClass
): Float {
    return when (height) {
        HeightClass.COMPACT -> 8f
        HeightClass.MEDIUM  -> 7f
        HeightClass.EXPANDED-> 5f
    }
}

private fun mediumMetrics(
    height: HeightClass
): Float {
    return when (height) {
        HeightClass.COMPACT -> 7f
        HeightClass.MEDIUM -> 6f
        HeightClass.EXPANDED -> 4f
    }
}

private fun expandedMetrics(
    height: HeightClass
): Float {
    return when (height) {
        HeightClass.COMPACT -> 6f
        HeightClass.MEDIUM  -> 4f
        HeightClass.EXPANDED-> 3f
    }
}
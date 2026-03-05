package com.scribble.it.feature_canvas.presentation.common.ui.adaptive.metrics

import androidx.compose.ui.unit.dp
import com.scribble.it.ui.adaptive.layoutConfig.LayoutConfiguration
import com.scribble.it.ui.adaptive.scale.ScreenScale
import com.scribble.it.ui.adaptive.scale.WidthClass

fun retrieveCanvasGridMetrics(
    scale: ScreenScale,
    layout: LayoutConfiguration
): CanvasGridMetrics {

    return when (layout.width) {
        WidthClass.COMPACT -> compactMetrics(scale)
        WidthClass.MEDIUM -> mediumMetrics()
        WidthClass.EXPANDED -> expandedMetrics()
        WidthClass.LARGE,
        WidthClass.EXTRA_LARGE -> TODO()
    }
}

private fun compactMetrics(s: ScreenScale): CanvasGridMetrics {
    return if (s.width > 450.dp) {
        CanvasGridMetrics(
            gridCellsCount = 3,
        )
    } else {
        CanvasGridMetrics(
            gridCellsCount = 2,
        )
    }

}

private fun mediumMetrics(): CanvasGridMetrics {
    return CanvasGridMetrics(
        gridCellsCount = 4,
    )
}

private fun expandedMetrics(): CanvasGridMetrics {
    return CanvasGridMetrics(
        gridCellsCount = 6,
    )
}

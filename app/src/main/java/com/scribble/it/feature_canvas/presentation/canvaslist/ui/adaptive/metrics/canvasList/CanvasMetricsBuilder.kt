package com.scribble.it.feature_canvas.presentation.canvaslist.ui.adaptive.metrics.canvasList

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.scribble.it.feature_canvas.presentation.common.ui.adaptive.metrics.retrieveCanvasAppBarMetrics
import com.scribble.it.feature_canvas.presentation.common.ui.adaptive.metrics.retrieveCanvasGridMetrics
import com.scribble.it.feature_canvas.presentation.common.ui.adaptive.metrics.retrieveCanvasItemMetrics
import com.scribble.it.ui.adaptive.layoutConfig.LayoutConfiguration
import com.scribble.it.ui.adaptive.scale.ScreenScale

@Composable
fun rememberCanvasMetricsBundle(
    layout: LayoutConfiguration,
    scale: ScreenScale,
    typography: Typography
): CanvasMetricsBundle {

    val screen = remember(layout, scale) {
        retrieveCanvasListScreenMetrics(scale, layout)
    }

    val grid = remember(layout, scale) {
        retrieveCanvasGridMetrics(scale, layout)
    }

    val item = remember(layout, scale, typography) {
        retrieveCanvasItemMetrics(scale, layout, typography)
    }

    val appBar = remember(layout, scale, typography) {
        retrieveCanvasAppBarMetrics(scale, layout, typography)
    }

    val fab = remember(layout, scale) {
        retrieveCanvasFloatingButtonMetrics(scale, layout)
    }

    val sortBar = remember(layout, scale) {
        retrieveCanvasSortBarMetrics(scale, layout)
    }

    return remember(screen, grid, item, appBar, fab, sortBar) {
        CanvasMetricsBundle(
            screen = screen,
            grid = grid,
            item = item,
            appBar = appBar,
            floatingButton = fab,
            sortBar = sortBar
        )
    }
}
package com.scribble.it.feature_canvas.presentation.common.state

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import com.scribble.it.feature_canvas.presentation.common.ui.adaptive.metrics.CanvasGridMetrics
import com.scribble.it.feature_canvas.presentation.common.ui.adaptive.metrics.CanvasItemMetrics

sealed interface CanvasViewModeConfig {
    data class Grid(
        val state: LazyGridState,
        val gridMetrics: CanvasGridMetrics? = null,
        val itemMetrics: CanvasItemMetrics? = null
    ): CanvasViewModeConfig

    data class List(
        val state: LazyListState,
        val itemMetrics: CanvasItemMetrics? = null
    ): CanvasViewModeConfig
}
package com.scribble.it.feature_canvas.presentation.common.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.scribble.it.feature_canvas.presentation.common.state.CanvasViewModeConfig

@Composable
fun CanvasShimmer(
    config: CanvasViewModeConfig
) {
    when (config) {
        is CanvasViewModeConfig.Grid -> {
            CanvasShimmerGrid(
                modifier = Modifier
                    .fillMaxSize(),
                lazyGridState = rememberLazyGridState(),
                gridCellsCount = config.gridMetrics!!.gridCellsCount
            )
        }
        is CanvasViewModeConfig.List -> {
            CanvasShimmerList(
                modifier = Modifier
                    .fillMaxSize(),
                lazyGridState = rememberLazyListState()
            )
        }
    }
}
package com.scribble.it.feature_canvas.presentation.common.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CanvasShimmerGrid(
    modifier: Modifier,
    lazyGridState: LazyGridState,
    gridCellsCount: Int
) {
    CanvasGridLayout(
        modifier = modifier,
        userScrollEnabled = false,
        state = lazyGridState,
        gridCellsCount = gridCellsCount
    ) {
        items(15) {
            CanvasGridShimmerItem(
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
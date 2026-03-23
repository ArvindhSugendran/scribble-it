package com.scribble.it.feature_canvas.presentation.common.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CanvasShimmerList(
    modifier: Modifier,
    lazyGridState: LazyListState,
) {
    CanvasListLayout (
        modifier = modifier,
        userScrollEnabled = false,
        state = lazyGridState,
    ) {
        items(15) {
            CanvasListShimmerItem(
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

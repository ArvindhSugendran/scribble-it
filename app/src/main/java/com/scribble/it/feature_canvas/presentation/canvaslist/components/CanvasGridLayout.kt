package com.scribble.it.feature_canvas.presentation.canvaslist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CanvasGridLayout(
    modifier: Modifier = Modifier,
    gridCellsCount: Int = 2,
    state: LazyGridState,
    userScrollEnabled: Boolean = true,
    contentPaddingValues: PaddingValues = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 30.dp),
    verticalSpacing: Dp = 12.dp,
    horizontalSpacing: Dp = 12.dp,
    content: LazyGridScope.() -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(gridCellsCount),
        state = state,
        userScrollEnabled = userScrollEnabled,
        verticalArrangement = Arrangement.spacedBy(verticalSpacing),
        horizontalArrangement = Arrangement.spacedBy(horizontalSpacing),
        contentPadding = contentPaddingValues,
        content = content
    )
}
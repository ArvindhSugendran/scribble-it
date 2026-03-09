package com.scribble.it.feature_canvas.presentation.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CanvasListLayout(
    modifier: Modifier = Modifier,
    state: LazyListState,
    userScrollEnabled: Boolean = true,
    contentPaddingValues: PaddingValues = PaddingValues(
        start = 16.dp,
        end = 16.dp,
        top = 16.dp,
        bottom = 30.dp
    ),
    verticalSpacing: Dp = 12.dp,
    content: LazyListScope.() -> Unit
) {
    LazyColumn (
        modifier = modifier.clipToBounds(),
        state = state,
        userScrollEnabled = userScrollEnabled,
        contentPadding = contentPaddingValues,
        verticalArrangement = Arrangement.spacedBy(verticalSpacing),
        content = content
    )
}
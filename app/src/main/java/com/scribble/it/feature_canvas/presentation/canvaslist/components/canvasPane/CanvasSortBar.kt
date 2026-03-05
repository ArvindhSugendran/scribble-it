package com.scribble.it.feature_canvas.presentation.canvaslist.components.canvasPane

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.scribble.it.feature_canvas.domain.model.operation.SortOption
import com.scribble.it.feature_canvas.domain.model.operation.displayName
import com.scribble.it.feature_canvas.presentation.canvaslist.ui.adaptive.metrics.canvasList.CanvasSortBarMetrics

@Composable
fun CanvasSortBar(
    modifier: Modifier = Modifier,
    metrics: CanvasSortBarMetrics,
    listState: LazyListState,
    sortOptions: List<SortOption>,
    selectedOption: SortOption,
    onOptionSelected: (SortOption) -> Unit,
) {

    val horizontalArrangement = if(metrics.allowScroll) {
        Arrangement.spacedBy(16.dp)
    } else {
        Arrangement.SpaceBetween
    }

    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp,
        shadowElevation = 6.dp
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .clipToBounds(),
            horizontalArrangement = horizontalArrangement,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            state = listState
        ) {
            items(
                items = sortOptions,
                key = { it }
            ) { option ->
                CanvasSortItem(
                    modifier = Modifier,
                    text = option.displayName(),
                    selected = option == selectedOption,
                    onClick = {
                        onOptionSelected(option)
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun CanvasSortBarPreview() {
    var selected by remember { mutableStateOf(SortOption.CREATED_DATE_DESC) }

    CanvasSortBar(
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxWidth(),
        metrics = CanvasSortBarMetrics(
            allowScroll = true
        ),
        listState = rememberLazyListState(),
        sortOptions = SortOption.entries.toList(),
        selectedOption = selected,
        onOptionSelected = {
            selected = it
        }
    )
}
package com.scribble.it.feature_canvas.presentation.common.components

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.scribble.it.feature_canvas.domain.model.canvasSummary.CanvasSummary
import com.scribble.it.feature_canvas.presentation.common.ui.adaptive.metrics.CanvasItemMetrics
import com.scribble.it.feature_canvas.presentation.common.ui.adaptive.metrics.CanvasGridMetrics
import com.scribble.it.feature_canvas.presentation.common.ui.adaptive.metrics.retrieveCanvasGridMetrics
import com.scribble.it.feature_canvas.presentation.common.ui.adaptive.metrics.retrieveCanvasItemMetrics
import com.scribble.it.ui.adaptive.layoutConfig.getLayoutConfiguration
import com.scribble.it.ui.adaptive.scale.ScreenScale
import com.scribble.it.ui.theme.typography
import kotlinx.coroutines.flow.flowOf

@Composable
fun CanvasList(
    modifier: Modifier = Modifier,
    items: LazyPagingItems<CanvasSummary>,
    gridState: LazyGridState,
    canvasGridMetrics: CanvasGridMetrics,
    itemMetrics: CanvasItemMetrics,
    isLoading: Boolean,
    isBlurred: Boolean,
    selectedIds: Set<Long>,
    selectedPreviewId: Long = -1,
    onLongClicked: (Long) -> Unit,
    onClicked: (Long) -> Unit,
) {

    if (isLoading) {
        CanvasShimmerGrid(
            modifier = modifier,
            lazyGridState = rememberLazyGridState(),
            gridCellsCount = canvasGridMetrics.gridCellsCount
        )
    } else {
        CanvasGrid(
            modifier = modifier,
            userScrollEnabled = !isBlurred,
            gridCellsCount = canvasGridMetrics.gridCellsCount,
            lazyGridState = gridState,
            itemMetrics = itemMetrics,
            selectedIds = selectedIds,
            selectedPreviewId = selectedPreviewId,
            pagingItems = items,
            onLongClicked = onLongClicked,
            onClicked = onClicked
        )
    }
}

@Composable
private fun CanvasGrid(
    modifier: Modifier,
    gridCellsCount: Int,
    userScrollEnabled: Boolean,
    lazyGridState: LazyGridState,
    itemMetrics: CanvasItemMetrics,
    selectedIds: Set<Long>,
    selectedPreviewId: Long,
    pagingItems: LazyPagingItems<CanvasSummary>,
    onClicked: (Long) -> Unit,
    onLongClicked: (Long) -> Unit
) {
    val animatedColumns by animateIntAsState(targetValue = gridCellsCount)

    CanvasGridLayout(
        modifier = modifier,
        userScrollEnabled = userScrollEnabled,
        state = lazyGridState,
        gridCellsCount = animatedColumns
    ) {
        items(
            count = pagingItems.itemCount,
            key = { index ->
                pagingItems[index]?.id ?: index
            }
        ) { index ->
            val item = pagingItems[index] ?: return@items
            CanvasListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateItem(),
                itemMetrics = itemMetrics,
                canvasSummary = item,
                isSelected = selectedIds.contains(item.id),
                isSelectedPreview = item.id == selectedPreviewId,
                onClicked = { onClicked(it) },
                onLongClicked = { onLongClicked(it) },
            )
        }
    }
}

@Composable
private fun CanvasShimmerGrid(
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
            CanvasListShimmerItem(
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
fun CanvasListPreview() {
    val isLoading by remember { mutableStateOf(true) }

    val sampleCanvas = CanvasSummary(
        id = 1,
        title = "My First Sketch",
        thumbnailPath = null,
        createdDate = System.currentTimeMillis(),
        modifiedDate = System.currentTimeMillis(),
        deletedDate = null
    )

    val sampleCanvases = List(12) { index ->
        sampleCanvas.copy(
            id = index.toLong(),
            title = "Sketch #$index"
        )
    }

    val fakePagingItems = flowOf(
        PagingData.from(sampleCanvases)
    ).collectAsLazyPagingItems()

    val configuration = LocalConfiguration.current

    val windowInfo = currentWindowAdaptiveInfo()
    val windowSizeClass = windowInfo.windowSizeClass

    val layoutConfig = remember(windowSizeClass, configuration) {
        getLayoutConfiguration(windowSizeClass, configuration)
    }

    BoxWithConstraints(
        modifier = Modifier
            .systemBarsPadding()
    ) {

        val scale = remember(maxWidth, maxHeight) {
            ScreenScale(
                width = maxWidth,
                height = maxHeight
            )
        }

        val canvasItemMetrics = remember(layoutConfig, scale, MaterialTheme.typography) {
            retrieveCanvasItemMetrics(
                scale = scale,
                layout = layoutConfig,
                typography = typography
            )
        }

        val canvasGridMetrics = remember(layoutConfig, scale) {
            retrieveCanvasGridMetrics(
                scale = scale,
                layout = layoutConfig
            )
        }

        CanvasList(
            modifier = Modifier
                .fillMaxSize(),
            isLoading = isLoading,
            isBlurred = false,
            items = fakePagingItems,
            gridState = rememberLazyGridState(),
            canvasGridMetrics = canvasGridMetrics,
            itemMetrics = canvasItemMetrics,
            selectedIds = setOf(1, 2, 3),
            onClicked = {},
            onLongClicked = {},
        )
    }
}
package com.scribble.it.feature_canvas.presentation.canvaslist.components

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import com.scribble.it.feature_canvas.presentation.canvaslist.ui.CanvasItemMetrics
import com.scribble.it.feature_canvas.presentation.canvaslist.ui.CanvasScreenMetrics
import com.scribble.it.feature_canvas.presentation.canvaslist.ui.retrieveCanvasItemMetrics
import com.scribble.it.feature_canvas.presentation.canvaslist.ui.retrieveCanvasListScreenMetrics
import com.scribble.it.feature_canvas.presentation.canvaslist.viewmodel.CanvasTestingSummary
import com.scribble.it.ui.adaptive.layoutConfig.getLayoutConfiguration
import com.scribble.it.ui.adaptive.scale.ScreenScale
import com.scribble.it.ui.theme.typography
import kotlinx.coroutines.delay

@Composable
fun CanvasList(
    modifier: Modifier = Modifier,
    sampleCanvases: List<CanvasTestingSummary>,
    gridState: LazyGridState,
    screenMetrics: CanvasScreenMetrics,
    itemMetrics: CanvasItemMetrics,
    isLoading: Boolean,
    isBlurred: Boolean,
    selectedIds: Set<Int>,
    onLoadingChanged: (Boolean) -> Unit,
    onLongClicked: (Int) -> Unit,
    onClicked: (Int) -> Unit
) {
    LaunchedEffect(Unit) {
        delay(6000)
        onLoadingChanged(false)
    }

    if(isLoading) {
        CanvasShimmerGrid(
            modifier = modifier,
            lazyGridState = rememberLazyGridState(),
            gridCellsCount = screenMetrics.gridCellsCount
        )
    }
    else {
        CanvasGrid(
            modifier = modifier,
            userScrollEnabled = !isBlurred,
            gridCellsCount = screenMetrics.gridCellsCount,
            lazyGridState = gridState,
            itemMetrics = itemMetrics,
            selectedIds = selectedIds,
            sampleCanvases = sampleCanvases,
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
    selectedIds: Set<Int>,
    sampleCanvases: List<CanvasTestingSummary>,
    onClicked: (Int) -> Unit,
    onLongClicked: (Int) -> Unit
) {
    CanvasGridLayout(
        modifier = modifier,
        userScrollEnabled = userScrollEnabled,
        state = lazyGridState,
        gridCellsCount = gridCellsCount
    ) {
        items(
            items = sampleCanvases,
            key = {
                sampleCanvas -> sampleCanvas.id ?: sampleCanvas.hashCode()
            }
        ) { sampleCanvas ->
            CanvasListItem(
                modifier = Modifier.fillMaxWidth(),
                itemMetrics = itemMetrics,
                canvasSummary = sampleCanvas,
                isSelected = selectedIds.contains(sampleCanvas.id),
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
        items(10) {
            CanvasListShimmerItem(
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
fun CanvasListPreview() {
    var isLoading by remember { mutableStateOf(true) }

    val sampleCanvas = CanvasTestingSummary(
        id = 1,
        title = "My First Sketch",
        thumbnailPath = null,
        createdDate = System.currentTimeMillis(),
        modifiedDate = System.currentTimeMillis(),
        deletedAt = null
    )

    val sampleCanvases = List(12) {index ->
        sampleCanvas.copy(
            id = index,
            title = "Sketch #$index"
        )
    }

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

        val canvasScreenMetrics = remember(layoutConfig, scale) {
            retrieveCanvasListScreenMetrics(
                scale = scale,
                layout = layoutConfig
            )
        }

        CanvasList(
            modifier = Modifier
                .fillMaxSize(),
            isLoading = isLoading,
            isBlurred = false,
            sampleCanvases = sampleCanvases,
            gridState = rememberLazyGridState(),
            screenMetrics = canvasScreenMetrics,
            itemMetrics = canvasItemMetrics,
            selectedIds = setOf(1,2,3),
            onLoadingChanged = { isLoading = it },
            onClicked = {},
            onLongClicked = {},
        )
    }
}
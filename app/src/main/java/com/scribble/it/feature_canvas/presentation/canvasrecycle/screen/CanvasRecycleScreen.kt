package com.scribble.it.feature_canvas.presentation.canvasrecycle.screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeGestures
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Deselect
import androidx.compose.material.icons.filled.KeyboardDoubleArrowDown
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.RestoreFromTrash
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.scribble.it.feature_canvas.domain.model.canvasSummary.CanvasSummary
import com.scribble.it.feature_canvas.presentation.canvaslist.screen.mirroredHorizontalInset
import com.scribble.it.feature_canvas.presentation.common.action.BulkRecycleActionType
import com.scribble.it.feature_canvas.presentation.common.state.TopBarMode
import com.scribble.it.feature_canvas.presentation.canvasrecycle.action.CanvasRecycleAction
import com.scribble.it.feature_canvas.presentation.canvasrecycle.components.CanvasRecycleDeleteBottomBar
import com.scribble.it.feature_canvas.presentation.canvasrecycle.event.CanvasRecycleEvent
import com.scribble.it.feature_canvas.presentation.canvasrecycle.state.CanvasRecycleUiState
import com.scribble.it.feature_canvas.presentation.canvasrecycle.ui.adaptive.metrics.retrieveCanvasRecycleScreenMetrics
import com.scribble.it.feature_canvas.presentation.common.action.BulkActionEvent
import com.scribble.it.feature_canvas.presentation.common.action.CanvasItemClickType
import com.scribble.it.feature_canvas.presentation.common.action.CanvasItemInteraction
import com.scribble.it.feature_canvas.presentation.common.components.CanvasAppBar
import com.scribble.it.feature_canvas.presentation.common.components.CanvasDeleteBar
import com.scribble.it.feature_canvas.presentation.common.components.CanvasList
import com.scribble.it.feature_canvas.presentation.common.components.ConfirmationDialog
import com.scribble.it.feature_canvas.presentation.common.ui.adaptive.metrics.retrieveCanvasAppBarMetrics
import com.scribble.it.feature_canvas.presentation.common.ui.adaptive.metrics.retrieveCanvasGridMetrics
import com.scribble.it.feature_canvas.presentation.common.ui.adaptive.metrics.retrieveCanvasItemMetrics
import com.scribble.it.ui.adaptive.layoutConfig.getLayoutConfiguration
import com.scribble.it.ui.adaptive.scale.HeightClass
import com.scribble.it.ui.adaptive.scale.ScreenScale
import com.scribble.it.ui.adaptive.scale.WidthClass
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CanvasRecycleScreen(
    modifier: Modifier = Modifier,
    uiState: CanvasRecycleUiState,
    eventsFlow: Flow<CanvasRecycleEvent>,
    recyclePagingFlow: Flow<PagingData<CanvasSummary>>,
    onAction: (CanvasRecycleAction) -> Unit,
    onNavigateBackToCanvasList: () -> Unit,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {
    val configuration = LocalConfiguration.current
    val typography = MaterialTheme.typography

    val windowInfo = currentWindowAdaptiveInfo()
    val windowSizeClass = windowInfo.windowSizeClass

    val layoutConfig = remember(windowSizeClass, configuration) {
        getLayoutConfiguration(windowSizeClass, configuration)
    }

    val canvasRecycleGridState = rememberLazyGridState()
    val pullRefreshState = rememberPullToRefreshState()

    val recyclePagingItems = recyclePagingFlow.collectAsLazyPagingItems()

    val showDeleteBar = uiState.topBarMode == TopBarMode.DELETE
    val showAppBar = uiState.topBarMode == TopBarMode.DEFAULT
    val showBottomDeleteBar = uiState.selectedIds.isNotEmpty()

    val isWideLayoutWithCompactHeight =
        layoutConfig.width >= WidthClass.MEDIUM && layoutConfig.height != HeightClass.COMPACT

    val recycleEmpty by remember {
        derivedStateOf {
            recyclePagingItems.loadState.refresh is LoadState.NotLoading && recyclePagingItems.itemCount == 0
        }
    }

    LaunchedEffect(eventsFlow, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            eventsFlow.collectLatest {
            }
        }
    }

    BoxWithConstraints(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.statusBars)
            .then(
                if (isWideLayoutWithCompactHeight) {
                    Modifier.windowInsetsPadding(
                        WindowInsets.safeGestures.only(
                            WindowInsetsSides.Horizontal + WindowInsetsSides.Top
                        )
                    )
                } else {
                    Modifier
                        .windowInsetsPadding(
                            WindowInsets.displayCutout.only(
                                WindowInsetsSides.Top
                            )
                        )
                        .mirroredHorizontalInset(WindowInsets.displayCutout)
                }
            )
    ) {
        val scale = remember(maxWidth, maxHeight) {
            ScreenScale(width = maxWidth, height = maxHeight)
        }
        val canvasRecycleScreenMetrics = remember(layoutConfig, scale) {
            retrieveCanvasRecycleScreenMetrics(scale = scale, layout = layoutConfig)
        }
        val canvasGridMetrics = remember(layoutConfig, scale) {
            retrieveCanvasGridMetrics(scale = scale, layout = layoutConfig)
        }
        val canvasItemMetrics = remember(layoutConfig, scale, typography) {
            retrieveCanvasItemMetrics(scale = scale, layout = layoutConfig, typography = typography)
        }
        val canvasAppBarMetrics = remember(layoutConfig, scale, typography) {
            retrieveCanvasAppBarMetrics(
                scale = scale, layout = layoutConfig, typography = typography
            )
        }

        Column {
            AnimatedVisibility(
                visible = showDeleteBar,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                CanvasDeleteBar(
                    modifier = Modifier
                        .padding(canvasRecycleScreenMetrics.horizontalPadding)
                        .fillMaxWidth(),
                    selectedCount = uiState.selectedIds.size,
                    onCloseDeleteBar = {
                        onAction(CanvasRecycleAction.CloseDeleteBar)
                    },
                    actions = {
                        AnimatedContent(targetState = uiState.isAllSelected) { allSelected ->
                            IconButton(
                                modifier = Modifier.size(35.dp),
                                onClick = {
                                    onAction(
                                        CanvasRecycleAction.SelectAllCanvases(
                                            targetIds = recyclePagingItems.itemSnapshotList.items.mapNotNull { it.id }
                                                .toSet()
                                        )
                                    )
                                }
                            ) {
                                Icon(
                                    imageVector = if (allSelected)
                                        Icons.Default.Deselect
                                    else
                                        Icons.Default.SelectAll,
                                    contentDescription = "Select All / Deselect All",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.fillMaxSize(0.7f)
                                )
                            }
                        }
                    }
                )
            }

            AnimatedVisibility(
                visible = showAppBar,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                CanvasAppBar(
                    modifier = Modifier
                        .fillMaxWidth(),
                    metrics = canvasAppBarMetrics,
                    appName = "Trash",
                    leadingIcon = {
                        IconButton(
                            modifier = Modifier
                                .size(35.dp),
                            onClick = {
                                onNavigateBackToCanvasList()
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Default.KeyboardDoubleArrowDown,
                                contentDescription = "Close Draw Screen",
                                tint = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    },
                    actions = {

                        //Restore Button
                        IconButton(
                            modifier = Modifier.size(35.dp),
                            onClick = {
                                onAction(
                                    CanvasRecycleAction.Bulk(
                                        BulkActionEvent.Request(
                                            targetIds = recyclePagingItems.itemSnapshotList.items.mapNotNull { it.id }
                                                .toSet(),
                                            type = BulkRecycleActionType.RESTORE_ALL
                                        )
                                    )
                                )
                            },
                            enabled = !uiState.isInitialLoading && !uiState.isRefreshing
                        ) {
                            Icon(
                                imageVector = Icons.Default.Restore,
                                contentDescription = "Restore canvas",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.fillMaxSize(0.7f)
                            )
                        }

                        // Delete button
                        IconButton(
                            modifier = Modifier.size(35.dp),
                            onClick = {
                                onAction(
                                    CanvasRecycleAction.Bulk(
                                        BulkActionEvent.Request(
                                            targetIds = recyclePagingItems.itemSnapshotList.items.mapNotNull { it.id }
                                                .toSet(),
                                            type = BulkRecycleActionType.DELETE_ALL
                                        )
                                    )
                                )
                            },
                            enabled = !uiState.isInitialLoading && !uiState.isRefreshing
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete canvas",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.fillMaxSize(0.7f)
                            )
                        }
                    },
                )
            }

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {

                if (recycleEmpty && !uiState.isInitialLoading && !uiState.isRefreshing) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(canvasRecycleScreenMetrics.illustrationSize)
                                .aspectRatio(1f)
                                .background(
                                    color = Color.Transparent,
                                    shape = MaterialTheme.shapes.medium,
                                )
                                .innerShadow(
                                    shape = MaterialTheme.shapes.medium,
                                    shadow = Shadow(
                                        radius = 10.dp,
                                        spread = 4.dp,
                                        color = Color(0x40000000),
                                        offset = DpOffset(x = 3.dp, 4.dp)
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.RestoreFromTrash,
                                contentDescription = "Illustration",
                                modifier = Modifier.fillMaxSize(0.6f),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "Nothing In Trash",
                            modifier = Modifier
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.headlineMedium.copy(
                                color = MaterialTheme.colorScheme.primary,
                            )
                        )
                    }
                }

                PullToRefreshBox(
                    modifier = Modifier,
                    state = pullRefreshState,
                    isRefreshing = uiState.isRefreshing,
                    onRefresh = {
                        onAction(CanvasRecycleAction.RefreshData)
                    },
                ) {

                    CanvasList(
                        modifier = Modifier
                            .fillMaxSize(),
                        isLoading = uiState.isInitialLoading,
                        isBlurred = false,
                        items = recyclePagingItems,
                        gridState = canvasRecycleGridState,
                        canvasGridMetrics = canvasGridMetrics,
                        itemMetrics = canvasItemMetrics,
                        selectedIds = uiState.selectedIds,
                        onClicked = { id ->
                            onAction(
                                CanvasRecycleAction.CanvasItemInteractionAction(
                                    CanvasItemInteraction(
                                        clickType = CanvasItemClickType.CLICK,
                                        selectedId = id,
                                        availableItemIds = recyclePagingItems.itemSnapshotList.items.mapNotNull { it.id }
                                            .toSet()
                                    )
                                )
                            )
                        },
                        onLongClicked = { id ->
                            onAction(
                                CanvasRecycleAction.CanvasItemInteractionAction(
                                    CanvasItemInteraction(
                                        clickType = CanvasItemClickType.LONG_CLICK,
                                        selectedId = id,
                                        availableItemIds = recyclePagingItems.itemSnapshotList.items.mapNotNull { it.id }
                                            .toSet()
                                    )
                                )
                            )
                        },
                    )

                    ConfirmationDialog(
                        state = uiState.dialogState,
                        onConfirm = {
                            onAction(
                                CanvasRecycleAction.Bulk(
                                    BulkActionEvent.Confirm
                                )
                            )
                        },
                        onDismiss = {
                            onAction(
                                CanvasRecycleAction.Bulk(
                                    BulkActionEvent.Dismiss
                                )
                            )
                        }
                    )
                }
            }

            AnimatedVisibility(
                visible = showDeleteBar && showBottomDeleteBar,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                CanvasRecycleDeleteBottomBar(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onRestore = {
                        onAction(
                            CanvasRecycleAction.Bulk(
                                BulkActionEvent.Request(
                                    type = BulkRecycleActionType.RESTORE_ALL,
                                    targetIds = uiState.selectedIds
                                )
                            )
                        )
                    },
                    onDelete = {
                        onAction(
                            CanvasRecycleAction.Bulk(
                                BulkActionEvent.Request(
                                    type = BulkRecycleActionType.DELETE_ALL,
                                    targetIds = uiState.selectedIds
                                )
                            )
                        )
                    }
                )
            }
        }
    }

    BackHandler(enabled = uiState.topBarMode != TopBarMode.DEFAULT) {
        when (uiState.topBarMode) {
            TopBarMode.DELETE -> {
                onAction(CanvasRecycleAction.CloseDeleteBar)
                return@BackHandler
            }

            else -> {}
        }
    }

}
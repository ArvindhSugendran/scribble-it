package com.scribble.it.feature_canvas.presentation.canvaslist.screen

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.systemGestureExclusion
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.ImageNotSupported
import androidx.compose.material.icons.filled.Recycling
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.scribble.it.feature_canvas.domain.model.canvasSummary.CanvasSummary
import com.scribble.it.feature_canvas.presentation.canvasdraw.navigation.CanvasDrawRoute
import com.scribble.it.feature_canvas.presentation.canvaslist.action.CanvasListAction
import com.scribble.it.feature_canvas.presentation.common.components.CanvasAppBar
import com.scribble.it.feature_canvas.presentation.common.components.CanvasDeleteBar
import com.scribble.it.feature_canvas.presentation.canvaslist.components.canvasPane.CanvasFloatingButton
import com.scribble.it.feature_canvas.presentation.common.components.CanvasList
import com.scribble.it.feature_canvas.presentation.canvaslist.components.canvasPane.CanvasSearchBar
import com.scribble.it.feature_canvas.presentation.canvaslist.components.canvasPane.CanvasSortBar
import com.scribble.it.feature_canvas.presentation.canvaslist.components.previewPane.PreviewContent
import com.scribble.it.feature_canvas.presentation.canvaslist.event.CanvasListEvent
import com.scribble.it.feature_canvas.presentation.canvaslist.state.CanvasListUiState
import com.scribble.it.feature_canvas.presentation.canvaslist.state.DialogUiState
import com.scribble.it.feature_canvas.presentation.canvaslist.state.FabUiState
import com.scribble.it.feature_canvas.presentation.canvaslist.state.ListUiState
import com.scribble.it.feature_canvas.presentation.canvaslist.state.PaneMode
import com.scribble.it.feature_canvas.presentation.canvaslist.state.PaneUiState
import com.scribble.it.feature_canvas.presentation.canvaslist.state.StableAdaptiveState
import com.scribble.it.feature_canvas.presentation.canvaslist.state.TopBarUiState
import com.scribble.it.feature_canvas.presentation.canvaslist.ui.adaptive.environment.LocalCanvasMetrics
import com.scribble.it.feature_canvas.presentation.canvaslist.ui.adaptive.environment.LocalPreviewCanvasMetrics
import com.scribble.it.feature_canvas.presentation.canvaslist.ui.adaptive.environment.LocalVisiblePaneWidth
import com.scribble.it.feature_canvas.presentation.canvaslist.ui.adaptive.metrics.canvasList.rememberCanvasMetricsBundle
import com.scribble.it.feature_canvas.presentation.canvaslist.ui.adaptive.metrics.preview.rememberPreviewScreenMetrics
import com.scribble.it.feature_canvas.presentation.common.state.TopBarMode
import com.scribble.it.feature_canvas.presentation.canvaslist.ui.adaptive.pane.AdaptiveMetricsPane
import com.scribble.it.feature_canvas.presentation.common.action.BulkActionEvent
import com.scribble.it.feature_canvas.presentation.common.action.BulkRecycleActionType
import com.scribble.it.feature_canvas.presentation.common.action.CanvasItemClickType
import com.scribble.it.feature_canvas.presentation.common.action.CanvasItemInteraction
import com.scribble.it.feature_canvas.presentation.common.components.CanvasShimmer
import com.scribble.it.feature_canvas.presentation.common.components.ConfirmationDialog
import com.scribble.it.feature_canvas.presentation.common.state.CanvasViewMode
import com.scribble.it.feature_canvas.presentation.common.state.CanvasViewModeConfig
import com.scribble.it.ui.adaptive.layoutConfig.getLayoutConfiguration
import com.scribble.it.ui.adaptive.layoutConfig.getPaneLayoutConfiguration
import com.scribble.it.ui.adaptive.layoutConfig.isTwoPaneAllowed
import com.scribble.it.ui.adaptive.scale.ScreenScale
import com.scribble.it.ui.adaptive.scale.WidthClass
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.roundToInt

@Composable
fun CanvasListScreen(
    modifier: Modifier = Modifier,
    uiState: CanvasListUiState,
    basePagingFlow: Flow<PagingData<CanvasSummary>>,
    searchPagingFlow: Flow<PagingData<CanvasSummary>>,
    eventsFlow: Flow<CanvasListEvent>,
    onAction: (CanvasListAction) -> Unit,
    onNavigateToCanvasDraw: (CanvasDrawRoute) -> Unit,
    onNavigateToCanvasRecycle: () -> Unit,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {
    val configuration = LocalConfiguration.current
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val typography = MaterialTheme.typography
    val density = LocalDensity.current

    val windowInfo = currentWindowAdaptiveInfo()
    val windowSizeClass = windowInfo.windowSizeClass

    val layoutConfig = remember(windowSizeClass, configuration) {
        getLayoutConfiguration(windowSizeClass, configuration)
    }

    val focusRequester = remember { FocusRequester() }
    val stableAdaptiveState = rememberStableAdaptiveState()

    val canvasGridState = rememberLazyGridState()
    val canvasListState = rememberLazyListState()

    val searchResultsGridState = rememberLazyGridState()
    val searchResultsListState = rememberLazyListState()

    val sortOptionsListState = rememberLazyListState()
    val pullRefreshState = rememberPullToRefreshState()
    val imeVisible = rememberImeVisible()

    val basePagingItems = basePagingFlow.collectAsLazyPagingItems()
    val searchPagingItems = searchPagingFlow.collectAsLazyPagingItems()

    var searchCycleStarted by remember { mutableStateOf(false) }
    var isTwoPane by remember { mutableStateOf(false) }

    val paneState = uiState.pane
    val topBarState = uiState.topBar
    val listState = uiState.list
    val fabState = uiState.fab
    val dialogState = uiState.dialog

    val showDeleteBar = topBarState.topBarMode == TopBarMode.DELETE
    val showAppBar = topBarState.topBarMode == TopBarMode.DEFAULT
    val showSortBar = topBarState.topBarMode == TopBarMode.SORT
    val isSearchMode = topBarState.topBarMode == TopBarMode.SEARCH

    val isWideLayout = layoutConfig.width >= WidthClass.MEDIUM

    val blurRadius by animateDpAsState(
        targetValue = if (isSearchMode) 16.dp else 0.dp, label = "blurRadius"
    )

    val searchPadding by animateDpAsState(
        targetValue = if (isSearchMode && !isWideLayout) 0.dp else 16.dp,
        label = "searchBar Padding"
    )

    val cornerRadiusForSearchBar by animateDpAsState(
        targetValue = if (isSearchMode) 0.dp else 24.dp, label = "searchBar cornerRadius"
    )

    val baseEmpty by remember {
        derivedStateOf {
            basePagingItems.loadState.refresh is LoadState.NotLoading && basePagingItems.itemCount == 0
        }
    }

    val selectedIndex = remember(
        paneState.selectedScribbleId, basePagingItems.itemSnapshotList
    ) {
        basePagingItems.itemSnapshotList.items.indexOfFirst { it.id == paneState.selectedScribbleId }
    }

    val textReveal = remember { androidx.compose.animation.core.Animatable(0f) }
    var hasAnimated by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(listState.isInitialLoading) {
        if (listState.isInitialLoading) {
            hasAnimated = false
            textReveal.snapTo(0f)
        } else {
            if (hasAnimated) {
                textReveal.snapTo(1f)
            }
            if (!hasAnimated) {
                textReveal.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = 1000,
                        easing = LinearOutSlowInEasing
                    )
                )
                hasAnimated = true
            }
        }
    }

    LaunchedEffect(isTwoPane) {
        if (!isTwoPane) {
            onAction(
                CanvasListAction.OnPaneChanged(
                    PaneMode.SinglePane
                )
            )
        } else {
            if (paneState.paneMode is PaneMode.SinglePane) {
                onAction(
                    CanvasListAction.OnPaneChanged(
                        PaneMode.TwoPane.Split
                    )
                )
            }
        }
    }

    LaunchedEffect(imeVisible) {
        if (!imeVisible) {
            focusManager.clearFocus()
        }
    }

    LaunchedEffect(isSearchMode) {
        if (isSearchMode) {
            focusRequester.requestFocus()
            onAction(
                CanvasListAction.QueryChange(
                    topBarState.query.copy(
                        selection = TextRange(
                            topBarState.query.text.length
                        )
                    )
                )
            )
        }
    }

    LaunchedEffect(showSortBar) {
        if (showSortBar) {
            sortOptionsListState.scrollToItem(topBarState.sortOptions.indexOf(topBarState.sortOption))
        }
    }

    LaunchedEffect(topBarState.query.text) {
        snapshotFlow { searchPagingItems.loadState.refresh }
            .map { it::class }
            .distinctUntilChanged()
            .collect { stateClass ->
                when (stateClass) {
                    LoadState.Loading::class -> {
                        searchCycleStarted = true
                    }

                    LoadState.NotLoading::class -> {
                        if (searchCycleStarted) {
                            val count = searchPagingItems.itemCount
                            val queryNotBlank = topBarState.query.text.isNotBlank()
                            val showEmpty = count == 0 && queryNotBlank

                            onAction(CanvasListAction.UpdateShowSearchEmpty(showEmptyResultsText = showEmpty))

                            if (count > 0) {
                                searchResultsGridState.scrollToItem(0)
                            }
                            searchCycleStarted = false
                        }
                    }

                    LoadState.Error::class -> {
                        onAction(CanvasListAction.UpdateShowSearchEmpty(showEmptyResultsText = false))
                        searchCycleStarted = false
                    }
                }
            }
    }

    LaunchedEffect(basePagingItems.loadState.refresh) {
        snapshotFlow { basePagingItems.loadState.refresh }
            .distinctUntilChanged()
            .collectLatest { stateClass ->
                when (stateClass) {
                    is LoadState.Loading -> {}

                    is LoadState.NotLoading -> {
                        if (listState.shouldScrollToTop) {
                            when (listState.canvasViewMode) {
                                CanvasViewMode.LIST -> {
                                    snapshotFlow { canvasListState.layoutInfo.totalItemsCount }
                                        .filter { it > 0 }
                                        .first()
                                    withFrameNanos {  }
                                    canvasListState.animateScrollToItem(0)
                                }
                                CanvasViewMode.GRID -> {
                                    snapshotFlow { canvasGridState.layoutInfo.totalItemsCount }
                                        .filter { it > 0 }
                                        .first()
                                    withFrameNanos {  }
                                    canvasGridState.animateScrollToItem(0, 0)
                                }
                            }
                            onAction(CanvasListAction.ConsumedScrollToTop)
                        } else {
                            onAction(CanvasListAction.OnLoadingCompleted)
                        }
                    }

                    is LoadState.Error -> {}
                }
            }
    }

    var previousMode by rememberSaveable { mutableStateOf<CanvasViewMode?>(null) }

    LaunchedEffect(selectedIndex, listState.canvasViewMode) {

        val offsetPx = with(density) { 50.dp.roundToPx() }
        when (listState.canvasViewMode) {
            CanvasViewMode.LIST -> {
                if (selectedIndex != -1) {
                    canvasListState.animateScrollToItem(selectedIndex)
                } else {
                    if (previousMode != listState.canvasViewMode) {
                        canvasListState.animateScrollToItem(0)
                    }
                }
            }

            CanvasViewMode.GRID -> {
                if (selectedIndex != -1) {
                    canvasGridState.animateScrollToItem(selectedIndex, scrollOffset = (-offsetPx))
                } else {
                    if (previousMode != listState.canvasViewMode) {
                        canvasGridState.animateScrollToItem(0)
                    }
                }
            }
        }

        previousMode = listState.canvasViewMode
    }

    LaunchedEffect(searchResultsGridState, searchResultsListState) {
        launch {
            snapshotFlow { searchResultsGridState.isScrollInProgress }.collectLatest { isScrolling ->
                if (isScrolling) keyboardController?.hide()
            }
        }

        launch {
            snapshotFlow { searchResultsListState.isScrollInProgress }.collectLatest { isScrolling ->
                if (isScrolling) keyboardController?.hide()
            }
        }
    }

    LaunchedEffect(canvasGridState, canvasListState) {
        launch {
            snapshotFlow { canvasGridState.layoutInfo }.collectLatest { layoutInfo ->

                val first = layoutInfo.visibleItemsInfo.firstOrNull()
                val last = layoutInfo.visibleItemsInfo.lastOrNull()

                when {
                    first?.index == 0 && first.offset.y == 0 -> onAction(
                        CanvasListAction.UpdateFabExpansion(
                            true
                        )
                    )

                    last != null && last.index == layoutInfo.totalItemsCount - 1 && last.offset.y + last.size.height <= layoutInfo.viewportEndOffset -> onAction(
                        CanvasListAction.UpdateFabExpansion(false)
                    )
                }
            }
        }

        launch {
            snapshotFlow { canvasListState.layoutInfo }.collectLatest { layoutInfo ->

                val first = layoutInfo.visibleItemsInfo.firstOrNull()
                val last = layoutInfo.visibleItemsInfo.lastOrNull()

                when {
                    first?.index == 0 && first.offset == 0 -> onAction(
                        CanvasListAction.UpdateFabExpansion(
                            true
                        )
                    )

                    last != null && last.index == layoutInfo.totalItemsCount - 1 && last.offset + last.size <= layoutInfo.viewportEndOffset -> onAction(
                        CanvasListAction.UpdateFabExpansion(false)
                    )
                }
            }
        }
    }

    LaunchedEffect(eventsFlow, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            eventsFlow.collectLatest { event ->
                when (event) {
                    is CanvasListEvent.NavigateToCanvasDraw -> onNavigateToCanvasDraw(event.canvasDrawRoute)
                    is CanvasListEvent.NavigateToCanvasRecycle -> onNavigateToCanvasRecycle()
                }
            }
        }
    }

    val fabScrollConnection = remember {
        object : NestedScrollConnection {

            private var accumulatedScroll = 0f
            private val threshold = 200f

            override fun onPreScroll(
                available: Offset, source: NestedScrollSource
            ): Offset {

                val isScrollable =
                    canvasGridState.canScrollForward || canvasGridState.canScrollBackward

                if (!isScrollable) {
                    accumulatedScroll = 0f
                    return Offset.Zero
                }

                val deltaY = available.y
                if (abs(deltaY) < 2f) {
                    accumulatedScroll = 0f
                    return Offset.Zero
                }

                accumulatedScroll += deltaY

                when {
                    accumulatedScroll < -threshold -> {
                        onAction(CanvasListAction.UpdateFabExpansion(false))
                        accumulatedScroll = 0f
                    }

                    accumulatedScroll > threshold -> {
                        onAction(CanvasListAction.UpdateFabExpansion(true))
                        accumulatedScroll = 0f
                    }
                }

                return Offset.Zero
            }
        }
    }

    BoxWithConstraints(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.statusBars)
            .windowInsetsPadding(
                WindowInsets.displayCutout.only(
                    WindowInsetsSides.Top
                )
            )
            .mirroredHorizontalInset(WindowInsets.displayCutout)
    ) {

        isTwoPane = remember(maxWidth, maxHeight) {
            isTwoPaneAllowed(
                widthDp = maxWidth, heightDp = maxHeight
            )
        }

        AdaptiveSplitLayout(
            stableAdaptiveState = stableAdaptiveState,
            reveal = paneState.paneReveal,
            onRevealChange = { onAction(CanvasListAction.OnRevealChanged(it)) },
            isTwoPane = isTwoPane,
            onPaneStateChange = { onAction(CanvasListAction.OnPaneChanged(it)) },
            primaryPane = { (modifier, providedPaneWidth) ->
                AdaptiveMetricsPane(modifier = modifier, buildMetrics = { scale, layout ->
                    val metricsWidth = stableAdaptiveState.current()
                    if (metricsWidth != null) {
                        val adjustedScale = remember(metricsWidth, scale.height) {
                            ScreenScale(metricsWidth, scale.height)
                        }
                        val adjustedLayoutConfiguration = remember(adjustedScale) {
                            getPaneLayoutConfiguration(
                                adjustedScale.width, adjustedScale.height
                            )
                        }
                        rememberCanvasMetricsBundle(
                            layout = adjustedLayoutConfiguration,
                            scale = adjustedScale,
                            typography = typography
                        )
                    } else {
                        rememberCanvasMetricsBundle(
                            layout = layout,
                            scale = scale,
                            typography = typography
                        )
                    }
                }, provide = { metrics, content ->
                    CompositionLocalProvider(
                        LocalCanvasMetrics provides metrics,
                        providedPaneWidth
                    ) {
                        content()
                    }
                }, content = {
                    if (baseEmpty && !listState.isInitialLoading && !listState.isRefreshing) {
                        Text(
                            text = "Create your first Scribble ✏️",
                            modifier = Modifier
                                .fillMaxWidth()
                                .imePadding()
                                .align(Alignment.Center)
                                .drawWithContent {
                                    val revealWidth = size.width * textReveal.value
                                    clipRect(
                                        left = 0f,
                                        right = revealWidth
                                    ) {
                                        this@drawWithContent.drawContent()
                                    }
                                },
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.headlineMedium.copy(
                                color = MaterialTheme.colorScheme.primary,
                            )
                        )
                    }

                    val baseViewModeLayoutConfig: CanvasViewModeConfig =
                        when (listState.canvasViewMode) {
                            CanvasViewMode.LIST -> {
                                CanvasViewModeConfig.List(
                                    state = canvasListState,
                                )
                            }

                            CanvasViewMode.GRID -> {
                                CanvasViewModeConfig.Grid(
                                    state = canvasGridState,
                                )
                            }
                        }

                    val searchResultsViewModeLayoutConfig: CanvasViewModeConfig =
                        when (listState.canvasViewMode) {
                            CanvasViewMode.LIST -> {
                                CanvasViewModeConfig.List(
                                    state = searchResultsListState,
                                )
                            }

                            CanvasViewMode.GRID -> {
                                CanvasViewModeConfig.Grid(
                                    state = searchResultsGridState,
                                )
                            }
                        }

                    CanvasListContent(
                        modifier = Modifier.fillMaxSize(),
                        paneState = paneState,
                        topBarState = topBarState,
                        listState = listState,
                        fabState = fabState,
                        dialogState = dialogState,
                        basePagingItems = basePagingItems,
                        searchPagingItems = searchPagingItems,
                        sortOptionsListState = sortOptionsListState,
                        baseViewModeConfig = baseViewModeLayoutConfig,
                        searchResultsViewModeConfig = searchResultsViewModeLayoutConfig,
                        pullRefreshState = pullRefreshState,
                        blurRadius = blurRadius,
                        searchPadding = searchPadding,
                        cornerRadiusForSearchBar = cornerRadiusForSearchBar,
                        showDeleteBar = showDeleteBar,
                        showAppBar = showAppBar,
                        showSortBar = showSortBar,
                        isSearchMode = isSearchMode,
                        onAction = onAction,
                        focusRequester = focusRequester,
                        keyboardController = keyboardController,
                        focusManager = focusManager,
                        fabScrollConnection = fabScrollConnection
                    )
                }
                )
            },
            secondaryPane = { (modifier, providedPaneWidth) ->
                AdaptiveMetricsPane(modifier = modifier, buildMetrics = { scale, layout ->
                    rememberPreviewScreenMetrics(
                        layout = layout,
                        scale = scale,
                    )
                }, provide = { metrics, content ->
                    CompositionLocalProvider(
                        LocalPreviewCanvasMetrics provides metrics,
                        providedPaneWidth
                    ) {
                        content()
                    }
                }, content = {
                    PreviewPane(
                        modifier = Modifier.fillMaxSize(),
                        pagingItems = basePagingItems,
                        selectedIndex = selectedIndex,
                        onPageChanged = { id ->
                            onAction(CanvasListAction.OnPageChanged(id))
                        },
                        onAction = { action ->
                            onAction(action)
                        }
                    )
                })
            },
        )
    }

    BackHandler(
        enabled = topBarState.topBarMode != TopBarMode.DEFAULT || (paneState.paneMode != PaneMode.TwoPane.Split && paneState.paneMode != PaneMode.SinglePane)
    ) {
        when (topBarState.topBarMode) {
            TopBarMode.DELETE -> {
                onAction(CanvasListAction.CloseDeleteBar)
                return@BackHandler
            }

            TopBarMode.SEARCH -> {
                keyboardController?.hide()
                focusManager.clearFocus()
                onAction(CanvasListAction.UpdateTopBarMode(TopBarMode.DEFAULT))
                onAction(CanvasListAction.QueryChange(TextFieldValue("")))
                return@BackHandler
            }

            TopBarMode.SORT -> {
                onAction(CanvasListAction.UpdateTopBarMode(TopBarMode.DEFAULT))
                return@BackHandler
            }

            else -> {}
        }

        if (paneState.paneMode != PaneMode.TwoPane.Split && paneState.paneMode != PaneMode.SinglePane) {
            stableAdaptiveState.commit(
                0.5f, null
            )
            onAction(
                CanvasListAction.OnPaneChanged(
                    PaneMode.TwoPane.Split
                )
            )
            return@BackHandler
        }
    }
}

@Composable
fun AdaptiveSplitLayout(
    stableAdaptiveState: StableAdaptiveState,
    reveal: Float,
    onRevealChange: (Float) -> Unit,
    isTwoPane: Boolean,
    onPaneStateChange: (PaneMode) -> Unit,
    modifier: Modifier = Modifier,
    primaryPane: @Composable (Pair<Modifier, ProvidedValue<Dp>>) -> Unit,
    secondaryPane: @Composable (Pair<Modifier, ProvidedValue<Dp>>) -> Unit
) {
    val density = LocalDensity.current
    var adaptiveModifier: Modifier
    var local: ProvidedValue<Dp>

    val animatedReveal by animateFloatAsState(reveal)

    BoxWithConstraints(
        modifier.fillMaxSize()
    ) {

        if (!isTwoPane) {
            adaptiveModifier = Modifier.fillMaxSize()
            local = LocalVisiblePaneWidth provides maxWidth

            primaryPane(adaptiveModifier to local)
            return@BoxWithConstraints
        }

        val maxWidthPx = with(density) { maxWidth.toPx() }
        val halfWidthDp = maxWidth / 2

        stableAdaptiveState.halfWidth(halfWidthDp)

        val handleBoxWidth = 30.dp
        val handleBoxWidthPx = with(density) { handleBoxWidth.toPx() }

        val dividerX = maxWidthPx * animatedReveal
        val dividerDp = with(density) { dividerX.toDp() }

        val leftWidthDp = if (animatedReveal >= 0.5f) {
            dividerDp - handleBoxWidth / 2
        } else {
            halfWidthDp - handleBoxWidth / 2
        }

        val rightWidthDp = if (animatedReveal <= 0.5f) {
            val rightWidthPx = maxWidthPx - dividerX
            with(density) { rightWidthPx.toDp() } + handleBoxWidth / 2
        } else {
            halfWidthDp + handleBoxWidth / 2
        }

        val slidePx = if (animatedReveal >= 0.5f) {
            val maxRightPx = maxWidthPx - handleBoxWidthPx
            ((animatedReveal - 0.5f) * maxRightPx)
        } else 0f

        val visibleLeftWidthDp = dividerDp - handleBoxWidth / 2

        adaptiveModifier = Modifier.fillMaxSize()
        local = LocalVisiblePaneWidth provides visibleLeftWidthDp

        val primaryModifier =
            Modifier
                .width(leftWidthDp)
                .fillMaxHeight()
                .align(Alignment.CenterStart)
                .clipToBounds()

        val secondaryModifier =
            Modifier
                .width(rightWidthDp)
                .fillMaxHeight()
                .align(Alignment.CenterEnd)
                .clipToBounds()

        // PRIMARY PANE
        Box(modifier = primaryModifier) {
            primaryPane(adaptiveModifier to local)
        }

        // SECONDARY PANE
        Row(
            modifier = Modifier
                .zIndex(1f)
                .offset { IntOffset(slidePx.roundToInt(), 0) }
                .background(MaterialTheme.colorScheme.surface)
                .align(Alignment.TopEnd)
                .width(rightWidthDp)
                .fillMaxHeight(), verticalAlignment = Alignment.CenterVertically) {

            DragHandle(
                reveal = reveal,
                totalWidthPx = maxWidthPx,
                handleBoxWidth = handleBoxWidth,
                stableAdaptiveState = stableAdaptiveState,
                onRevealChange = {
                    onRevealChange(it)
                },
                onPaneStateChange = {
                    onPaneStateChange(it)
                })

            // SECONDARY
            Box(modifier = secondaryModifier) {
                secondaryPane(adaptiveModifier to local)
            }
        }
    }
}

@Composable
private fun CanvasListContent(
    modifier: Modifier = Modifier,
    paneState: PaneUiState,
    topBarState: TopBarUiState,
    listState: ListUiState,
    fabState: FabUiState,
    dialogState: DialogUiState,
    basePagingItems: LazyPagingItems<CanvasSummary>,
    searchPagingItems: LazyPagingItems<CanvasSummary>,
    baseViewModeConfig: CanvasViewModeConfig,
    searchResultsViewModeConfig: CanvasViewModeConfig,
    sortOptionsListState: LazyListState,
    pullRefreshState: PullToRefreshState,
    blurRadius: Dp,
    searchPadding: Dp,
    cornerRadiusForSearchBar: Dp,
    showDeleteBar: Boolean,
    showAppBar: Boolean,
    showSortBar: Boolean,
    isSearchMode: Boolean,
    onAction: (CanvasListAction) -> Unit,
    focusRequester: FocusRequester,
    keyboardController: SoftwareKeyboardController?,
    focusManager: FocusManager,
    fabScrollConnection: NestedScrollConnection
) {
    val metrics = LocalCanvasMetrics.current
    val visiblePaneWidth = LocalVisiblePaneWidth.current

    val canvasListScreenMetrics = metrics.screen
    val canvasGridMetrics = metrics.grid
    val canvasItemMetrics = metrics.item
    val canvasAppBarMetrics = metrics.appBar
    val canvasSortBarMetrics = metrics.sortBar
    val canvasFloatingButtonMetrics = metrics.floatingButton

    val baseModeConfig: CanvasViewModeConfig = when (baseViewModeConfig) {
        is CanvasViewModeConfig.Grid -> baseViewModeConfig.copy(
            gridMetrics = canvasGridMetrics,
            itemMetrics = canvasItemMetrics
        )

        is CanvasViewModeConfig.List -> baseViewModeConfig.copy(
            itemMetrics = canvasItemMetrics,
        )
    }

    val searchModeConfig: CanvasViewModeConfig = when (searchResultsViewModeConfig) {
        is CanvasViewModeConfig.Grid -> searchResultsViewModeConfig.copy(
            gridMetrics = canvasGridMetrics,
            itemMetrics = canvasItemMetrics
        )

        is CanvasViewModeConfig.List -> searchResultsViewModeConfig.copy(
            itemMetrics = canvasItemMetrics,
        )
    }

    Column(
        modifier = modifier
    ) {

        AnimatedVisibility(
            visible = showDeleteBar,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            CanvasDeleteBar(
                modifier = Modifier
                    .padding(canvasListScreenMetrics.horizontalPadding)
                    .fillMaxWidth(),
                selectedCount = listState.selectedIds.size,
                onCloseDeleteBar = {
                    onAction(CanvasListAction.CloseDeleteBar)
                }, actions = {
                    IconButton(modifier = Modifier.size(35.dp), onClick = {
                        onAction(
                            CanvasListAction.Bulk(
                                BulkActionEvent.Request(
                                    type = BulkRecycleActionType.RECYCLE_ALL,
                                    targetIds = listState.selectedIds
                                )
                            )
                        )
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete canvas",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.fillMaxSize(0.7f)
                        )
                    }
                })
        }

        AnimatedVisibility(
            visible = showSortBar,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            CanvasSortBar(
                modifier = Modifier
                    .padding(canvasListScreenMetrics.horizontalPadding)
                    .fillMaxWidth(),
                metrics = canvasSortBarMetrics,
                listState = sortOptionsListState,
                sortOptions = topBarState.sortOptions,
                selectedOption = topBarState.sortOption,
                onOptionSelected = { sortOption ->
                    onAction(CanvasListAction.UpdateSortOption(sortOption))
                },
            )
        }

        AnimatedVisibility(
            visible = showAppBar,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Box(
                modifier = Modifier.width(visiblePaneWidth)
            ) {
                CanvasAppBar(
                    modifier = Modifier.fillMaxWidth(),
                    metrics = canvasAppBarMetrics,
                    appName = "Scribble",
                    actions = {
                        IconButton(
                            modifier = Modifier.size(35.dp),
                            onClick = { onAction(CanvasListAction.RecycleBinClicked) },
                            enabled = !listState.isInitialLoading && !listState.isRefreshing
                        ) {
                            Icon(
                                imageVector = Icons.Default.Recycling,
                                contentDescription = "Recycle Bin",
                                tint = MaterialTheme.colorScheme.onSurface,
                            )
                        }

                        IconButton(
                            modifier = Modifier.size(35.dp),
                            onClick = { onAction(CanvasListAction.SortClicked) },
                            enabled = !listState.isInitialLoading && !listState.isRefreshing
                        ) {
                            Icon(
                                imageVector = Icons.Default.SwapVert,
                                contentDescription = "Sort",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        IconButton(
                            modifier = Modifier.size(35.dp),
                            onClick = { onAction(CanvasListAction.ToggleGridView) },
                            enabled = !listState.isInitialLoading && !listState.isRefreshing
                        ) {
                            AnimatedContent(targetState = listState.canvasViewMode) { viewMode ->
                                when (viewMode) {
                                    CanvasViewMode.LIST -> {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Default.ListAlt,
                                            contentDescription = "List View",
                                            tint = MaterialTheme.colorScheme.onSurface
                                        )
                                    }

                                    CanvasViewMode.GRID -> {
                                        Icon(
                                            imageVector = Icons.Default.GridView,
                                            contentDescription = "Grid View",
                                            tint = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }

                            }
                        }
                    },
                )
            }
        }

        CanvasSearchBar(
            modifier = Modifier
                .padding(
                    horizontal = searchPadding,
                    vertical = 10.dp
                )
                .fillMaxWidth(),
            focusRequester = focusRequester,
            cornerRadius = cornerRadiusForSearchBar,
            query = topBarState.query,
            isBlurred = isSearchMode,
            enabled = !(listState.isRefreshing || listState.isInitialLoading),
            onQueryChange = { query ->
                onAction(CanvasListAction.QueryChange(query))
            },
            onFocusChange = { focus ->
                if (focus) onAction(CanvasListAction.SearchMode)
            },
            onSearch = {
                keyboardController?.hide()
            },
            onCloseSearchBar = {
                keyboardController?.hide()
                focusManager.clearFocus()
                onAction(CanvasListAction.UpdateTopBarMode(TopBarMode.DEFAULT))
                onAction(CanvasListAction.QueryChange(TextFieldValue("")))
            })

        Box(
            modifier = Modifier.weight(1f)
        ) {
            PullToRefreshBox(
                modifier = Modifier,
                state = pullRefreshState,
                isRefreshing = listState.isRefreshing,
                onRefresh = {
                    onAction(CanvasListAction.RefreshData)
                },
            ) {
                if (listState.isInitialLoading) {
                    CanvasShimmer(baseModeConfig)
                } else {
                    CanvasList(
                        modifier = Modifier
                            .fillMaxSize()
                            .nestedScroll(fabScrollConnection)
                            .blur(blurRadius),
                        isBlurred = isSearchMode,
                        items = basePagingItems,
                        config = baseModeConfig,
                        selectedIds = listState.selectedIds,
                        selectedPreviewId = paneState.selectedScribbleId,
                        onClicked = { id ->
                            onAction(
                                CanvasListAction.CanvasItemInteractionAction(
                                    CanvasItemInteraction(
                                        clickType = CanvasItemClickType.CLICK,
                                        selectedId = id,
                                    )
                                )
                            )
                        },
                        onLongClicked = { id ->
                            onAction(
                                CanvasListAction.CanvasItemInteractionAction(
                                    CanvasItemInteraction(
                                        clickType = CanvasItemClickType.LONG_CLICK, selectedId = id
                                    )
                                )
                            )
                        },
                    )
                }

                ConfirmationDialog(state = dialogState.confirmationDialog, onConfirm = {
                    onAction(
                        CanvasListAction.Bulk(
                            BulkActionEvent.Confirm
                        )
                    )
                }, onDismiss = {
                    onAction(
                        CanvasListAction.Bulk(
                            BulkActionEvent.Dismiss
                        )
                    )
                })
            }

            androidx.compose.animation.AnimatedVisibility(
                visible = blurRadius == 16.dp,
                enter = fadeIn() + slideInVertically { it / 6 },
                exit = fadeOut() + slideOutVertically { it / 6 },
                modifier = Modifier.fillMaxSize()
            ) {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = searchPadding
                    )
                    .imePadding()
                    .pointerInput(Unit) {
                        detectTapGestures {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                            onAction(CanvasListAction.UpdateTopBarMode(TopBarMode.DEFAULT))
                            onAction(CanvasListAction.QueryChange(TextFieldValue("")))
                        }
                    }) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.18f))
                            .blur(10.dp)
                            .border(
                                width = 1.dp, color = Color.White.copy(alpha = 0.3f)
                            )
                    )

                    if (listState.showEmptyScribbles) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp)
                                .shadow(
                                    elevation = 12.dp,
                                    shape = RoundedCornerShape(25)
                                )
                                .align(Alignment.Center)
                        ) {

                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .background(Color.White.copy(alpha = 0.18f))
                                    .blur(10.dp)
                                    .border(
                                        width = 1.dp,
                                        color = Color.White,
                                        shape = RoundedCornerShape(25)
                                    )
                            )

                            Text(
                                text = "NO SCRIBBLES FOUND",
                                modifier = Modifier
                                    .padding(vertical = 4.dp)
                                    .align(Alignment.Center),
                                textAlign = TextAlign.Center,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                        }
                    }

                    CanvasList(
                        modifier = Modifier.fillMaxSize(),
                        isBlurred = false,
                        items = searchPagingItems,
                        config = searchModeConfig,
                        selectedIds = emptySet(),
                        onClicked = { id ->
                            onAction(
                                CanvasListAction.CanvasItemInteractionAction(
                                    CanvasItemInteraction(
                                        clickType = CanvasItemClickType.CLICK, selectedId = id
                                    )
                                )
                            )
                        },
                        onLongClicked = {},
                    )
                }
            }

            androidx.compose.animation.AnimatedVisibility(
                visible = showSortBar,
            ) {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures {
                            onAction(CanvasListAction.UpdateTopBarMode(TopBarMode.DEFAULT))
                        }
                    })
            }

            androidx.compose.animation.AnimatedVisibility(
                visible = !showDeleteBar && !isSearchMode && !showSortBar && !listState.isInitialLoading && !listState.isRefreshing,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut(),
                modifier = Modifier.align(Alignment.BottomEnd)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier.width(visiblePaneWidth)
                    ) {
                        CanvasFloatingButton(modifier = Modifier
                            .navigationBarsPadding()
                            .align(Alignment.BottomEnd)
                            .padding(end = 16.dp, bottom = 16.dp),
                            metrics = canvasFloatingButtonMetrics,
                            isFabExpanded = fabState.isFabExpanded,
                            onDrawClicked = {
                                onAction(CanvasListAction.DrawCanvasClicked)
                            })
                    }
                }
            }
        }
    }
}

@SuppressLint("FrequentlyChangingValue")
@Composable
private fun PreviewPane(
    modifier: Modifier = Modifier,
    pagingItems: LazyPagingItems<CanvasSummary>,
    selectedIndex: Int,
    onPageChanged: (Long) -> Unit,
    onAction: (CanvasListAction) -> Unit
) {

    val metrics = LocalPreviewCanvasMetrics.current
    val infoCardChange = metrics.infoCardStyleChange
    val illustrationSize = metrics.illustrationSize

    Box(
        modifier = modifier
    ) {
        if (pagingItems.itemCount == 0) {

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(illustrationSize)
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
                        imageVector = Icons.Default.ImageNotSupported,
                        contentDescription = "Illustration",
                        modifier = Modifier.fillMaxSize(0.6f),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "No Preview",
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

            return
        }

        if (selectedIndex < 0) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(illustrationSize)
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
                        imageVector = Icons.Default.Image,
                        contentDescription = "Illustration",
                        modifier = Modifier.fillMaxSize(0.6f),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Select a Scribble",
                    maxLines = 1,
                    overflow = TextOverflow.StartEllipsis,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.primary
                    )
                )
            }
            return
        }

        val pagerState = rememberPagerState(pageCount = { pagingItems.itemCount })

        LaunchedEffect(pagerState) {
            snapshotFlow { pagerState.currentPage }.distinctUntilChanged().collect {
                val pagingItemId = pagingItems[it]?.id
                if (pagingItemId != null) {
                    onPageChanged(pagingItemId)
                }
            }
        }

        LaunchedEffect(selectedIndex) {
            if (pagerState.currentPage != selectedIndex) {
                pagerState.scrollToPage(selectedIndex)
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
        ) { page ->
            PreviewContent(
                modifier = Modifier.fillMaxSize(),
                content = pagingItems[page] ?: return@HorizontalPager,
                pageOffset = pagerState.getOffsetDistanceInPages(page),
                infoCardChange = infoCardChange,
                closePreview = { onAction(CanvasListAction.ClosePreview) },
                editPreview = { onAction(CanvasListAction.EditPreview) }
            )
        }
    }
}

@Composable
private fun DragHandle(
    reveal: Float,
    totalWidthPx: Float,
    handleBoxWidth: Dp,
    onRevealChange: (Float) -> Unit,
    onPaneStateChange: (PaneMode) -> Unit,
    stableAdaptiveState: StableAdaptiveState,
) {
    var localReveal by remember { mutableFloatStateOf(reveal) }
    var handleWidth by remember { mutableStateOf(8.dp) }
    var isDragging by remember { mutableStateOf(false) }
    var idleJob by remember { mutableStateOf<Job?>(null) }

    val density = LocalDensity.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(reveal) {
        localReveal = reveal
    }

    Box(Modifier
        .width(handleBoxWidth)
        .height(70.dp)
        .systemGestureExclusion { coords ->
            Rect(
                0f, 0f, coords.size.width.toFloat(), coords.size.height.toFloat()
            )
        }
        .pointerInput(totalWidthPx) {
            detectHorizontalDragGestures(onHorizontalDrag = { change, dx ->
                change.consume()

                if (!isDragging) {
                    isDragging = true
                    val startWidth = totalWidthPx * localReveal
                    stableAdaptiveState.freeze(with(density) { startWidth.toDp() })
                }

                localReveal = (localReveal + dx / totalWidthPx).coerceIn(0f, 1f)

                onRevealChange(localReveal)
                handleWidth = 13.dp

                idleJob?.cancel()
                idleJob = scope.launch {
                    delay(100)
                    if (localReveal < 0.5f) {
                        stableAdaptiveState.commit(
                            0.5f, null
                        )
                    } else {
                        val currentWidth = totalWidthPx * localReveal
                        stableAdaptiveState.commit(localReveal, with(density) {
                            currentWidth.toDp()
                        })
                    }
                }
            }, onDragEnd = {
                localReveal = when {
                    localReveal > 0.8f -> 1f
                    localReveal < 0.2f -> 0f
                    else -> 0.5f
                }

                val finalState = when (localReveal) {
                    0f -> PaneMode.TwoPane.FullPreview
                    0.5f -> PaneMode.TwoPane.Split
                    1f -> PaneMode.TwoPane.FullList
                    else -> PaneMode.TwoPane.Split
                }

                handleWidth = 8.dp
                isDragging = false

                idleJob?.cancel()
                val finalWidth = totalWidthPx * localReveal
                stableAdaptiveState.commit(localReveal, with(density) {
                    finalWidth.toDp()
                })

                onPaneStateChange(finalState)
            })
        }) {
        Box(
            Modifier
                .align(Alignment.Center)
                .width(handleWidth)
                .height(50.dp)
                .background(
                    MaterialTheme.colorScheme.onSurfaceVariant, RoundedCornerShape(10.dp)
                )
        )
    }
}

@Composable
fun rememberStableAdaptiveState(): StableAdaptiveState {
    return remember { StableAdaptiveState() }
}

fun Long.toDateStrings(
    pattern: String = "MMM dd, yyyy hh:mm a"
): String {
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    return formatter.format(Date(this))
}

@Composable
fun rememberImeVisible(): Boolean {
    val density = LocalDensity.current
    val imeBottom = WindowInsets.ime.getBottom(density)
    return imeBottom > 0
}

@Composable
fun Modifier.mirroredHorizontalInset(
    insets: WindowInsets
): Modifier {
    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current

    val horizontal = with(density) {
        max(
            insets.getLeft(this, layoutDirection), insets.getRight(this, layoutDirection)
        ).toDp()
    }

    return this.padding(
        start = horizontal, end = horizontal
    )
}
package com.scribble.it.feature_canvas.presentation.canvaslist.screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeGestures
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.scribble.it.feature_canvas.presentation.canvaslist.action.CanvasListAction
import com.scribble.it.feature_canvas.presentation.canvaslist.components.CanvasAppBar
import com.scribble.it.feature_canvas.presentation.canvaslist.components.CanvasDeleteBar
import com.scribble.it.feature_canvas.presentation.canvaslist.components.CanvasFloatingButton
import com.scribble.it.feature_canvas.presentation.canvaslist.components.CanvasList
import com.scribble.it.feature_canvas.presentation.canvaslist.components.CanvasSearchBar
import com.scribble.it.feature_canvas.presentation.canvaslist.event.CanvasListEvent
import com.scribble.it.feature_canvas.presentation.canvaslist.state.CanvasListUiState
import com.scribble.it.feature_canvas.presentation.canvaslist.state.TopBarMode
import com.scribble.it.feature_canvas.presentation.canvaslist.ui.retrieveCanvasFloatingButtonMetrics
import com.scribble.it.feature_canvas.presentation.canvaslist.ui.retrieveCanvasItemMetrics
import com.scribble.it.feature_canvas.presentation.canvaslist.ui.retrieveCanvasListScreenMetrics
import com.scribble.it.feature_canvas.presentation.canvaslist.ui.retrieveCanvasTopBarMetrics
import com.scribble.it.ui.adaptive.layoutConfig.getLayoutConfiguration
import com.scribble.it.ui.adaptive.scale.HeightClass
import com.scribble.it.ui.adaptive.scale.ScreenScale
import com.scribble.it.ui.adaptive.scale.WidthClass
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.abs
import kotlin.math.max

@Composable
fun CanvasListScreen(
    modifier: Modifier = Modifier,
    uiState: CanvasListUiState,
    eventsFlow: Flow<CanvasListEvent>,
    onAction: (CanvasListAction) -> Unit,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {

    val configuration = LocalConfiguration.current
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val typography = MaterialTheme.typography

    val windowInfo = currentWindowAdaptiveInfo()
    val windowSizeClass = windowInfo.windowSizeClass

    val layoutConfig = remember(windowSizeClass, configuration) {
        getLayoutConfiguration(windowSizeClass, configuration)
    }

    val canvasListGridState = rememberLazyGridState()
    val searchResultsListGridState = rememberLazyGridState()
    val pullRefreshState = rememberPullToRefreshState()
    val imeVisible = rememberImeVisible()

    val showDeleteBar = uiState.topBarMode == TopBarMode.DELETE
    val showAppBar = uiState.topBarMode == TopBarMode.DEFAULT
    val isSearchMode = uiState.topBarMode == TopBarMode.SEARCH

    val blurRadius by animateDpAsState(
        targetValue = if (isSearchMode) 16.dp else 0.dp,
        label = "blurRadius"
    )

    val searchPadding by animateDpAsState(
        targetValue = if (isSearchMode) 0.dp else 16.dp,
        label = "searchBar Padding"
    )

    val cornerRadiusForSearchBar by animateDpAsState(
        targetValue = if (isSearchMode) 0.dp else 24.dp,
        label = "searchBar cornerRadius"
    )

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(imeVisible) {
        if (!imeVisible) {
            focusManager.clearFocus()
        }
    }

    LaunchedEffect(isSearchMode) {
        if (isSearchMode) {
            focusRequester.requestFocus()
            onAction(CanvasListAction.QueryChange(uiState.query.copy(selection = TextRange(uiState.query.text.length))))
        }
    }

    LaunchedEffect(uiState.searchResults) {
        searchResultsListGridState.scrollToItem(0)
    }

    LaunchedEffect(searchResultsListGridState) {
        snapshotFlow { searchResultsListGridState.isScrollInProgress }
            .collectLatest { isScrolling ->
                if(isScrolling) keyboardController?.hide()
            }
    }

    LaunchedEffect(canvasListGridState) {
        snapshotFlow { canvasListGridState.layoutInfo }
            .collectLatest { layoutInfo ->

                val first = layoutInfo.visibleItemsInfo.firstOrNull()
                val last = layoutInfo.visibleItemsInfo.lastOrNull()

                when {
                    first?.index == 0 && first.offset.y == 0 ->
                        onAction(CanvasListAction.UpdateFabExpansion(true))

                    last != null &&
                            last.index == layoutInfo.totalItemsCount - 1 &&
                            last.offset.y + last.size.height <= layoutInfo.viewportEndOffset ->
                        onAction(CanvasListAction.UpdateFabExpansion(false))
                }
            }
    }

    LaunchedEffect(eventsFlow, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            eventsFlow.collectLatest {

            }
        }
    }

    val fabScrollConnection = remember {
        object : NestedScrollConnection {

            private var accumulatedScroll = 0f
            private val threshold = 200f

            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource
            ): Offset {

                // Ignore tiny jitters
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
            .nestedScroll(fabScrollConnection)
            .then(
                if (layoutConfig.width >= WidthClass.MEDIUM && layoutConfig.height != HeightClass.COMPACT) {
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
        val canvasScreenMetrics = remember(layoutConfig, scale) {
            retrieveCanvasListScreenMetrics(scale = scale, layout = layoutConfig)
        }
        val canvasItemMetrics = remember(layoutConfig, scale, typography) {
            retrieveCanvasItemMetrics(scale = scale, layout = layoutConfig, typography = typography)
        }
        val canvasTopBarMetrics = remember(layoutConfig, scale, typography) {
            retrieveCanvasTopBarMetrics(
                scale = scale,
                layout = layoutConfig,
                typography = typography
            )
        }
        val canvasFloatingButtonMetrics = remember(layoutConfig, scale) {
            retrieveCanvasFloatingButtonMetrics(scale = scale, layout = layoutConfig)
        }

        Column {
            AnimatedVisibility(
                visible = showDeleteBar,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                CanvasDeleteBar(
                    modifier = Modifier.fillMaxWidth(),
                    selectedCount = uiState.selectedIds.size,
                    onCloseDeleteBar = {
                        onAction(CanvasListAction.CloseDeleteBar)
                    },
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
                    metrics = canvasTopBarMetrics,
                    appName = "Scribble",
                    isGridView = uiState.isGrid,
                    onRecycle = {},
                    onSort = {},
                    onToggleView = {
                        onAction(CanvasListAction.ToggleGridView)
                    },
                )
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
                query = uiState.query,
                isBlurred = isSearchMode,
                enabled = !(uiState.isRefreshing || uiState.isInitialLoading),
                onQueryChange = { query ->
                    onAction(CanvasListAction.QueryChange(query))
                },
                onFocusChange = { focus ->
                    if (focus) onAction(CanvasListAction.FocusChange)
                },
                onSearch = {
                    keyboardController?.hide()
                }
            )

            Box(
                modifier = Modifier
                    .weight(1f)
            ) {
                PullToRefreshBox(
                    modifier = Modifier,
                    state = pullRefreshState,
                    isRefreshing = uiState.isRefreshing,
                    onRefresh = {
                        onAction(CanvasListAction.RefreshData)
                    },
                ) {
                    CanvasList(
                        modifier = Modifier
                            .fillMaxSize()
                            .blur(blurRadius),
                        isLoading = uiState.isInitialLoading,
                        isBlurred = isSearchMode,
                        sampleCanvases = uiState.canvasList,
                        gridState = canvasListGridState,
                        screenMetrics = canvasScreenMetrics,
                        itemMetrics = canvasItemMetrics,
                        selectedIds = uiState.selectedIds,
                        onLoadingChanged = {
                            onAction(CanvasListAction.UpdateInitialLoading)
                        },
                        onClicked = { id ->
                            onAction(CanvasListAction.CanvasItemClicked(id))
                        },
                        onLongClicked = { id ->
                            onAction(CanvasListAction.CanvasItemLongClicked(id))
                        },
                    )
                }

                androidx.compose.animation.AnimatedVisibility(
                    visible = blurRadius == 16.dp,
                    enter = fadeIn() + slideInVertically { it / 6 },
                    exit = fadeOut() + slideOutVertically { it / 6 },
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Box {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.18f))
                                .blur(10.dp)
                                .border(
                                    width = 1.dp,
                                    color = Color.White.copy(alpha = 0.3f)
                                )
                                .pointerInput(Unit) {
                                    detectTapGestures {
                                        keyboardController?.hide()
                                        focusManager.clearFocus()
                                        onAction(CanvasListAction.UpdateTopBarMode(TopBarMode.DEFAULT))
                                        onAction(CanvasListAction.QueryChange(TextFieldValue("")))
                                    }
                                }
                        )

                        if (uiState.query.text.isNotEmpty() && uiState.searchResults.isNotEmpty()) {
                            CanvasList(
                                modifier = Modifier
                                    .fillMaxSize(),
                                isLoading = false,
                                isBlurred = false,
                                sampleCanvases = uiState.searchResults,
                                gridState = searchResultsListGridState,
                                screenMetrics = canvasScreenMetrics,
                                itemMetrics = canvasItemMetrics,
                                selectedIds = emptySet(),
                                onLoadingChanged = {
                                    onAction(CanvasListAction.UpdateInitialLoading)
                                },
                                onClicked = {},
                                onLongClicked = {}
                            )
                        } else if (uiState.searchResults.isEmpty()) {
                            Text(
                                text = "NO RESULTS FOUND",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .imePadding()
                                    .align(Alignment.Center),
                                textAlign = TextAlign.Center,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    color = MaterialTheme.colorScheme.onSurface,
                                )
                            )
                        }
                    }
                }

                androidx.compose.animation.AnimatedVisibility(
                    visible = !showDeleteBar && !isSearchMode,
                    enter = fadeIn() + scaleIn(),
                    exit = fadeOut() + scaleOut(),
                    modifier = Modifier.align(Alignment.BottomEnd)
                ) {
                    CanvasFloatingButton(
                        modifier = Modifier
                            .navigationBarsPadding()
                            .align(Alignment.BottomEnd)
                            .padding(end = 16.dp, bottom = 16.dp),
                        metrics = canvasFloatingButtonMetrics,
                        isFabExpanded = uiState.isFabExpanded
                    )
                }
            }
        }
    }

    BackHandler(enabled = uiState.topBarMode != TopBarMode.DEFAULT) {
        when (uiState.topBarMode) {
            TopBarMode.DELETE -> {
                onAction(CanvasListAction.CloseDeleteBar)
            }

            TopBarMode.SEARCH -> {
                keyboardController?.hide()
                focusManager.clearFocus()
                onAction(CanvasListAction.UpdateTopBarMode(TopBarMode.DEFAULT))
                onAction(CanvasListAction.QueryChange(TextFieldValue("")))
            }

            else -> Unit
        }
    }
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
            insets.getLeft(this, layoutDirection),
            insets.getRight(this, layoutDirection)
        ).toDp()
    }

    return this.padding(
        start = horizontal,
        end = horizontal
    )
}
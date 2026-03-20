package com.scribble.it.feature_canvas.presentation.canvasdraw.screen

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.scribble.it.feature_canvas.domain.model.stroke.CanvasStroke
import com.scribble.it.feature_canvas.presentation.canvasdraw.action.CanvasDrawAction
import com.scribble.it.feature_canvas.presentation.canvasdraw.components.CanvasDrawActionButtons
import com.scribble.it.feature_canvas.presentation.canvasdraw.components.CanvasDrawAppBar
import com.scribble.it.feature_canvas.presentation.canvasdraw.components.CanvasDrawStokeOptions
import com.scribble.it.feature_canvas.presentation.canvasdraw.components.CanvasReplayControls
import com.scribble.it.feature_canvas.presentation.canvasdraw.components.CanvasViewport
import com.scribble.it.feature_canvas.presentation.canvasdraw.components.PaperSurface
import com.scribble.it.feature_canvas.presentation.canvasdraw.event.CanvasDrawEvent
import com.scribble.it.feature_canvas.presentation.canvasdraw.state.CanvasDrawUiState
import com.scribble.it.feature_canvas.presentation.canvasdraw.state.ReplayState
import com.scribble.it.feature_canvas.presentation.canvasdraw.ui.adaptive.metrics.retrieveCanvasDrawActionButtonsMetrics
import com.scribble.it.feature_canvas.presentation.canvasdraw.ui.adaptive.metrics.retrieveCanvasDrawAppBarMetrics
import com.scribble.it.feature_canvas.presentation.canvasdraw.ui.adaptive.metrics.retrieveCanvasViewportMetrics
import com.scribble.it.ui.adaptive.layoutConfig.getLayoutConfiguration
import com.scribble.it.ui.adaptive.scale.ScreenScale
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun CanvasDrawScreen(
    modifier: Modifier = Modifier,
    uiState: CanvasDrawUiState,
    eventsFlow: Flow<CanvasDrawEvent>,
    onAction: (CanvasDrawAction) -> Unit,
    strokeColorPalette: List<Color>,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onNavigateBackToCanvasList: () -> Unit
) {

    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    val focusManager = LocalFocusManager.current

    val windowInfo = currentWindowAdaptiveInfo()
    val windowSizeClass = windowInfo.windowSizeClass

    val layoutConfig = remember(windowSizeClass, configuration) {
        getLayoutConfiguration(windowSizeClass, configuration)
    }

    val imeVisible = rememberImeVisible()

    val canvasContentState = uiState.canvasContentState
    val viewportState = uiState.viewportUiState
    val topBarState = uiState.topBarUiState
    val drawActionState = uiState.drawActionButtonsUiState
    val replay = uiState.replayUiState

    val blurRadius by animateDpAsState(
        targetValue = if (drawActionState.showStrokeOptions) 16.dp else 0.dp,
        label = "blurRadius"
    )

    LaunchedEffect(imeVisible) {
        if (!imeVisible) {
            focusManager.clearFocus()
        }
    }

    LaunchedEffect(eventsFlow, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            eventsFlow.collectLatest { event ->
                when (event) {
                    CanvasDrawEvent.NavigateBack -> onNavigateBackToCanvasList()
                }
            }
        }
    }

    Box(
        modifier = modifier
            .windowInsetsPadding(
                WindowInsets.safeDrawing.only(
                    WindowInsetsSides.Horizontal
                )
            )
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(
                        WindowInsetsSides.Top
                    )
                )
        ) {
            val scale = remember(maxWidth, maxHeight) {
                ScreenScale(width = maxWidth, height = maxHeight)
            }
            val canvasDrawAppBarMetrics = remember(layoutConfig, scale) {
                retrieveCanvasDrawAppBarMetrics(scale = scale, layout = layoutConfig)
            }
            val canvasDrawActionButtonsMetrics = remember(layoutConfig, scale) {
                retrieveCanvasDrawActionButtonsMetrics(scale = scale, layout = layoutConfig)
            }

            Column(
                modifier = modifier
                    .fillMaxSize()
            ) {

                androidx.compose.animation.AnimatedVisibility(
                    visible = replay.replayState == ReplayState.IDLE,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically(),
                ) {
                    CanvasDrawAppBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .zIndex(1f),
                        metrics = canvasDrawAppBarMetrics,
                        title = topBarState.canvasTitle,
                        drawingEnabled = topBarState.drawingEnabled,
                        onTitleChange = { canvasTitle ->
                            onAction(CanvasDrawAction.OnTitleChange(canvasTitle))
                        },
                        onDrawingEnable = {
                            onAction(CanvasDrawAction.EnableDrawing)
                        },
                        onBackPressed = {
                            onAction(CanvasDrawAction.OnBackPressed)
                        },
                        animate = {
                            onAction(CanvasDrawAction.Replay(ReplayState.PLAYING))
                        }
                    )
                }

                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .zIndex(0f)
                        .clipToBounds()
                ) {
                    val viewportScale = remember(maxWidth, maxHeight) {
                        ScreenScale(width = maxWidth, height = maxHeight)
                    }

                    val canvasViewportMetrics =
                        remember(
                            viewportScale,
                            layoutConfig,
                            canvasContentState.canvasDrawing.pageFormat
                        ) {
                            retrieveCanvasViewportMetrics(
                                scale = viewportScale,
                                layout = layoutConfig,
                                pageFormat = canvasContentState.canvasDrawing.pageFormat
                            )
                        }

                    val rootModifier = Modifier
                        .zIndex(1f)
                        .fillMaxHeight()
                        .blur(blurRadius)
                        .align(Alignment.TopEnd)

                    CanvasViewport(
                        modifier = Modifier
                            .zIndex(0f)
                            .fillMaxSize(),
                        maxWidth = maxWidth,
                        maxHeight = maxHeight,
                        offsetFraction = viewportState.offsetFraction,
                        metrics = canvasViewportMetrics,
                        replayState = replay.replayState,
                        onOffsetFractionChange = { offset: Offset ->
                            onAction(CanvasDrawAction.UpdateOffsetFraction(offset))
                        },
                        content = { transform ->
                            PaperSurface(
                                modifier = Modifier
                                    .graphicsLayer {
                                        scaleX = transform.scale
                                        scaleY = transform.scale
                                        translationX = transform.offsetPx.x
                                        translationY = transform.offsetPx.y
                                    }
                                    .size(transform.viewportSize)
                                    .blur(blurRadius),
                                drawingEnabled = topBarState.drawingEnabled,
                                brushSize = canvasContentState.brushSize.mm,
                                strokeColor = canvasContentState.strokeColor,
                                pageColor = Color(canvasContentState.canvasDrawing.pageColor),
                                canvasStrokes = canvasContentState.canvasDrawing.canvasStrokes,
                                replayTrigger = replay.replayTrigger,
                                replayState = replay.replayState,
                                onUpdateCanvasStrokes = { canvasStroke: CanvasStroke ->
                                    onAction(CanvasDrawAction.UpdateStrokes(canvasStroke))
                                },
                                onOffsetFractionChange = { offset: Offset ->
                                    onAction(CanvasDrawAction.UpdateOffsetFraction(offset))
                                },
                                onCancelCurrentStroke = {
                                    onAction(CanvasDrawAction.CancelLastStroke)
                                },
                                onReplay = {
                                    onAction(CanvasDrawAction.Replay(it))
                                }
                            )
                        }
                    )

                    androidx.compose.animation.AnimatedVisibility(
                        visible = replay.replayState == ReplayState.IDLE,
                        enter = fadeIn() + slideInHorizontally(
                            initialOffsetX = { it }
                        ),
                        exit = fadeOut() + slideOutHorizontally(
                            targetOffsetX = { it }
                        ),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            if (drawActionState.showActionButtonsOption) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.Black.copy(alpha = 0.7f))
                                        .pointerInput(Unit) {
                                            detectTapGestures {
                                                onAction(
                                                    CanvasDrawAction.ShowActionButtonsOption(
                                                        false
                                                    )
                                                )
                                            }
                                        }
                                )
                            }

                            if (drawActionState.showStrokeOptions) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .pointerInput(Unit) {
                                            detectTapGestures {
                                                onAction(
                                                    CanvasDrawAction.ShowStrokeOptions(
                                                        false
                                                    )
                                                )
                                            }
                                        }
                                )
                            }

                            CanvasDrawActionButtons(
                                modifier = rootModifier,
                                metrics = canvasDrawActionButtonsMetrics,
                                strokeOptions = drawActionState.showStrokeOptions,
                                showStrokeOptions = { show ->
                                    onAction(CanvasDrawAction.ShowStrokeOptions(show))
                                },
                                actionButtonsOption = drawActionState.showActionButtonsOption,
                                showActionButtonsOption = { show ->
                                    onAction(CanvasDrawAction.ShowActionButtonsOption(show))
                                },
                                undoDrawing = { onAction(CanvasDrawAction.UndoDrawing) },
                                redoDrawing = { onAction(CanvasDrawAction.RedoDrawing) },
                                clearDrawing = { onAction(CanvasDrawAction.ClearDrawing) },
                            )

                            androidx.compose.animation.AnimatedVisibility(
                                visible = blurRadius == 16.dp,
                                enter = fadeIn() + scaleIn(),
                                exit = fadeOut() + scaleOut(),
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CanvasDrawStokeOptions(
                                        modifier = Modifier
                                            .zIndex(2f)
                                            .pointerInput(Unit) {
                                                detectTapGestures { }
                                            },
                                        selectedBrush = canvasContentState.brushSize,
                                        selectedPageColor = Color(canvasContentState.canvasDrawing.pageColor),
                                        selectedStrokeColor = canvasContentState.strokeColor,
                                        strokeColorPalette = strokeColorPalette.filter {
                                            it != Color(
                                                canvasContentState.canvasDrawing.pageColor
                                            )
                                        },
                                        onSelectBrush = { brush ->
                                            onAction(CanvasDrawAction.ChangeBrushSize(brush))
                                        },
                                        onSelectPageColor = { pageColor ->
                                            onAction(CanvasDrawAction.ChangePageColor(pageColor))
                                        },
                                        onSelectStrokeColor = { strokeColor ->
                                            onAction(CanvasDrawAction.ChangeStrokeColor(strokeColor))
                                        }
                                    )
                                }
                            }
                        }
                    }

                    if (replay.replayState != ReplayState.IDLE) {
                        var previousReplayState by rememberSaveable { mutableStateOf(ReplayState.IDLE) }
                        var controlsVisible by rememberSaveable { mutableStateOf(false) }
                        var autoHideJob by remember { mutableStateOf<Job?>(null) }
                        val scope = rememberCoroutineScope()

                        val activity = LocalActivity.current as ComponentActivity
                        var isAppInForeground by remember { mutableStateOf(true) }

                        fun restartAutoHide() {
                            Log.d("JOB", "CALLED RESTART")

                            if(isAppInForeground) {
                                autoHideJob?.cancel()
                                autoHideJob = scope.launch {
                                    delay(2000)
                                    controlsVisible = false
                                    Log.d("JOB", "DID THE JOB")
                                }
                            }
                        }

                        DisposableEffect(activity) {
                            val observer = LifecycleEventObserver { _, event ->
                                when (event) {
                                    Lifecycle.Event.ON_PAUSE -> {
                                        if (!activity.isChangingConfigurations) {
                                            Log.d(
                                                "JOB",
                                                "ON_PAUSE"
                                            )
                                            isAppInForeground = false
                                            autoHideJob?.cancel()
                                        }
                                    }

                                    Lifecycle.Event.ON_RESUME -> {
                                        if (!isAppInForeground) {
                                            Log.d(
                                                "JOB",
                                                "ON_RESUME"
                                            )
                                            isAppInForeground = true
                                            controlsVisible = true
                                        } else {
                                            if(controlsVisible) restartAutoHide()
                                        }
                                    }

                                    else -> Unit
                                }
                            }

                            activity.lifecycle.addObserver(observer)
                            onDispose { activity.lifecycle.removeObserver(observer) }
                        }

                        LaunchedEffect(replay.replayState) {
                            Log.d(
                                "JOB",
                                "App In ForeGround: $isAppInForeground " +
                                        "REPLAY STATE: ${replay.replayState} " +
                                        "PREVIOUS REPLAY STATE: $previousReplayState"
                            )
                            if (isAppInForeground && previousReplayState != replay.replayState) {
                                controlsVisible = true
                                previousReplayState = replay.replayState
                                restartAutoHide()
                            } else {
                                if(controlsVisible) restartAutoHide()
                            }
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .pointerInput(Unit) {
                                    detectTapGestures {
                                        if (controlsVisible) {
                                            controlsVisible = false
                                            autoHideJob?.cancel()
                                        } else {
                                            controlsVisible = true
                                            restartAutoHide()
                                        }
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            androidx.compose.animation.AnimatedVisibility(
                                visible = controlsVisible,
                                enter = fadeIn(),
                                exit = fadeOut()
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.Black.copy(alpha = 0.7f))
                                )
                            }

                            androidx.compose.animation.AnimatedVisibility(
                                visible = controlsVisible,
                                enter = fadeIn() + scaleIn(),
                                exit = fadeOut() + scaleOut(),
                                modifier = Modifier.zIndex(1f)
                            ) {
                                CanvasReplayControls(
                                    replayState = replay.replayState,
                                    onReplay = { onAction(CanvasDrawAction.RestartReplay) },
                                    onPlay = { onAction(CanvasDrawAction.Replay(ReplayState.PLAYING)) },
                                    onPause = { onAction(CanvasDrawAction.Replay(ReplayState.PAUSED)) },
                                    onStop = { onAction(CanvasDrawAction.Replay(ReplayState.IDLE)) }
                                )
                            }
                        }
                    }
                }
            }
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(WindowInsets.statusBars.statusBarHeight(density))
                .background(MaterialTheme.colorScheme.surface),
            tonalElevation = 6.dp,
            content = { },
        )
    }

    BackHandler(enabled = drawActionState.showStrokeOptions) {
        onAction(CanvasDrawAction.ShowStrokeOptions(false))
    }

    BackHandler(enabled = !drawActionState.showStrokeOptions) {
        onAction(CanvasDrawAction.OnBackPressed)
    }
}

@Composable
fun rememberImeVisible(): Boolean {
    val density = LocalDensity.current
    val imeBottom = WindowInsets.ime.getBottom(density)
    return imeBottom > 0
}


fun WindowInsets.statusBarHeight(density: Density): Dp {
    return with(density) { getTop(this).toDp() }
}
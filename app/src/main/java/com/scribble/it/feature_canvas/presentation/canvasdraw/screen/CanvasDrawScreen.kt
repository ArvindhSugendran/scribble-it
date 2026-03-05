package com.scribble.it.feature_canvas.presentation.canvasdraw.screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.scribble.it.feature_canvas.domain.model.stroke.CanvasStroke
import com.scribble.it.feature_canvas.presentation.canvasdraw.action.CanvasDrawAction
import com.scribble.it.feature_canvas.presentation.canvasdraw.components.CanvasDrawActionButtons
import com.scribble.it.feature_canvas.presentation.canvasdraw.components.CanvasDrawAppBar
import com.scribble.it.feature_canvas.presentation.canvasdraw.components.CanvasDrawStokeOptions
import com.scribble.it.feature_canvas.presentation.canvasdraw.components.CanvasViewport
import com.scribble.it.feature_canvas.presentation.canvasdraw.components.PaperSurface
import com.scribble.it.feature_canvas.presentation.canvasdraw.event.CanvasDrawEvent
import com.scribble.it.feature_canvas.presentation.canvasdraw.state.CanvasDrawUiState
import com.scribble.it.feature_canvas.presentation.canvasdraw.ui.adaptive.metrics.retrieveCanvasDrawActionButtonsMetrics
import com.scribble.it.feature_canvas.presentation.canvasdraw.ui.adaptive.metrics.retrieveCanvasDrawAppBarMetrics
import com.scribble.it.feature_canvas.presentation.canvasdraw.ui.adaptive.metrics.retrieveCanvasViewportMetrics
import com.scribble.it.ui.adaptive.layoutConfig.getLayoutConfiguration
import com.scribble.it.ui.adaptive.scale.ScreenScale
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

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

    val blurRadius by animateDpAsState(
        targetValue = if (uiState.showStrokeOptions) 16.dp else 0.dp,
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
                CanvasDrawAppBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .zIndex(1f),
                    metrics = canvasDrawAppBarMetrics,
                    title = uiState.canvasTitle,
                    drawingEnabled = uiState.drawingEnabled,
                    onTitleChange = { canvasTitle ->
                        onAction(CanvasDrawAction.OnTitleChange(canvasTitle))
                    },
                    onDrawingEnable = {
                        onAction(CanvasDrawAction.EnableDrawing)
                    },
                    onBackPressed = {
                        onAction(CanvasDrawAction.OnBackPressed)
                    }
                )

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
                        remember(viewportScale, layoutConfig, uiState.canvasDrawing.pageFormat) {
                            retrieveCanvasViewportMetrics(
                                scale = viewportScale,
                                layout = layoutConfig,
                                pageFormat = uiState.canvasDrawing.pageFormat
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
                        offsetFraction = uiState.viewportUiState.offsetFraction,
                        metrics = canvasViewportMetrics,
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
                                drawingEnabled = uiState.drawingEnabled,
                                brushSize = uiState.brushSize.mm,
                                strokeColor = uiState.strokeColor,
                                pageColor = Color(uiState.canvasDrawing.pageColor),
                                canvasStrokes = uiState.canvasDrawing.canvasStrokes,
                                onUpdateCanvasStrokes = { canvasStroke: CanvasStroke ->
                                    onAction(CanvasDrawAction.UpdateStrokes(canvasStroke))
                                },
                                onOffsetFractionChange = { offset: Offset ->
                                    onAction(CanvasDrawAction.UpdateOffsetFraction(offset))
                                },
                                onCancelCurrentStroke = {
                                    onAction(CanvasDrawAction.CancelLastStroke)
                                }
                            )
                        }
                    )

                    CanvasDrawActionButtons(
                        modifier = rootModifier,
                        metrics = canvasDrawActionButtonsMetrics,
                        strokeOptions = uiState.showStrokeOptions,
                        showStrokeOptions = { show ->
                            onAction(CanvasDrawAction.ShowStrokeOptions(show))
                        },
                        actionButtonsOption = uiState.showActionButtonsOption,
                        showActionButtonsOption = { show ->
                            onAction(CanvasDrawAction.ShowActionButtonsOption(show))
                        },
                        undoDrawing = {onAction(CanvasDrawAction.UndoDrawing)},
                        redoDrawing = {onAction(CanvasDrawAction.RedoDrawing)},
                        clearDrawing = {onAction(CanvasDrawAction.ClearDrawing)},
                    )

                    androidx.compose.animation.AnimatedVisibility(
                        visible = blurRadius == 16.dp,
                        enter = fadeIn() + scaleIn(),
                        exit = fadeOut() + scaleOut(),
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Black.copy(alpha = 0.7f))
                                    .pointerInput(Unit) {
                                        detectTapGestures {
                                            onAction(CanvasDrawAction.ShowStrokeOptions(false))
                                        }
                                    }
                            )

                            CanvasDrawStokeOptions(
                                modifier = Modifier
                                    .zIndex(1f)
                                    .pointerInput(Unit) {
                                        awaitPointerEventScope {
                                            while (true) awaitPointerEvent()
                                        }
                                    },
                                selectedBrush = uiState.brushSize,
                                selectedPageColor = Color(uiState.canvasDrawing.pageColor),
                                selectedStrokeColor = uiState.strokeColor,
                                strokeColorPalette = strokeColorPalette.filter { it != Color(uiState.canvasDrawing.pageColor) },
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

    BackHandler(enabled = uiState.showStrokeOptions) {
        onAction(CanvasDrawAction.ShowStrokeOptions(false))
    }

    BackHandler(enabled = !uiState.showStrokeOptions) {
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
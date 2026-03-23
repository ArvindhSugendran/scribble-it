package com.scribble.it.feature_canvas.presentation.canvasdraw.components

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.changedToDown
import androidx.compose.ui.input.pointer.changedToUp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.scribble.it.feature_canvas.domain.model.stroke.CanvasStroke
import com.scribble.it.feature_canvas.domain.model.stroke.Dot
import com.scribble.it.feature_canvas.domain.model.stroke.PEN
import com.scribble.it.feature_canvas.presentation.canvasdraw.state.ReplayState
import com.scribble.it.ui.adaptive.scale.CanvasConstants

//UI → Logical:
//xLogical = xUI / uiWidth * PAGE_WIDTH == xUI * (PAGE_WIDTH / uiWidth)

//Logical → UI:
//xUI = xLogical / PAGE_WIDTH * uiWidth == xLogical * (uiWidth / PAGE_WIDTH)

data class PathSegment(
    val path: Path,
    val colorArgb: Int,
    val brushNormalized: Float,
)

data class StrokeCache(
    val segments: List<PathSegment>,
    val dots: List<Dot>,
    val totalSegments: Int
)

@Composable
fun PaperSurface(
    modifier: Modifier,
    drawingEnabled: Boolean,
    brushSize: Float,
    strokeColor: Color,
    pageColor: Color,
    replayState: ReplayState,
    replayTrigger: Int,
    canvasStrokes: List<CanvasStroke>,
    onUpdateCanvasStrokes: (CanvasStroke) -> Unit,
    onOffsetFractionChange: (Offset) -> Unit,
    onCancelCurrentStroke: () -> Unit,
    onReplay: (ReplayState) -> Unit
) {
    val metrics = LocalResources.current.displayMetrics
    val activity = LocalActivity.current as ComponentActivity

    var animationDuration by rememberSaveable { mutableIntStateOf(0) }
    var savedProgress by rememberSaveable { mutableFloatStateOf(1f) }
    var lastReplayTrigger by rememberSaveable { mutableIntStateOf(-1) }

    val animatedProgress = remember { Animatable(savedProgress) }

    var canvasSize by remember { mutableStateOf(IntSize.Zero) }
    var hasMoved by remember { mutableStateOf(false) }

    LaunchedEffect(replayState, replayTrigger) {
        if (replayState == ReplayState.PLAYING) {
            val strokeCount = canvasStrokes.count { it.penType == PEN.MOVE }
            val durationPerStroke = 500
            animationDuration = strokeCount * durationPerStroke

            if (animatedProgress.value == 1f) {
                animatedProgress.snapTo(0f)
            }

            if ((replayTrigger != lastReplayTrigger && replayTrigger > 0)) {
                lastReplayTrigger = replayTrigger

                animatedProgress.snapTo(0f)
                animatedProgress.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(animationDuration, easing = LinearEasing)
                )
            }

            val remainingProgress = 1f - animatedProgress.value
            val remainingDuration = (animationDuration * remainingProgress).toInt()

            animatedProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(remainingDuration, easing = LinearEasing)
            )
        } else if (replayState == ReplayState.IDLE) {
            animatedProgress.snapTo(1f)
        }
    }

    DisposableEffect(replayState) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    Log.d("PROGRESS_", activity.isChangingConfigurations.toString())

                    if (replayState == ReplayState.PLAYING && !activity.isChangingConfigurations) {
                        Log.d("PROGRESS_", "ON_PAUSE_CALLED")
                        onReplay(ReplayState.PAUSED)
                    }
                }

                else -> Unit
            }
        }

        activity.lifecycle.addObserver(observer)

        onDispose {
            activity.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(animatedProgress.value) {
        savedProgress = animatedProgress.value
        if (animatedProgress.value == 1f) {
            onReplay(ReplayState.IDLE)
        }
    }

    Log.d(
        "BRUSH_BEFORE",
        "Brush: ${brushSize}mm = ${brushSize * (metrics.densityDpi / 25.4f)}px @ ${metrics.densityDpi} dpi"
    )

    val cache = rememberStrokeCache(
        strokes = canvasStrokes,
        canvasWidth = canvasSize.width.toFloat(),
        canvasHeight = canvasSize.height.toFloat()
    )

    Box(
        modifier = modifier
            .background(pageColor)
            .border(1.dp, Color.LightGray)
            .clipToBounds()
            .onSizeChanged {
                canvasSize = it
            }
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(drawingEnabled, brushSize, strokeColor) {
                    if (drawingEnabled) {
                        awaitPointerEventScope {
                            while (true) {
                                val event = awaitPointerEvent()
                                val change = event.changes.firstOrNull() ?: continue

                                val size = this@pointerInput.size
                                val brushPx = brushSize * (metrics.densityDpi / 25.4f)
                                val brushNormalized = brushPx / CanvasConstants.PAGE_WIDTH

                                val canvasX =
                                    (change.position.x / size.width) * CanvasConstants.PAGE_WIDTH
                                val canvasY =
                                    (change.position.y / size.height) * CanvasConstants.PAGE_HEIGHT

                                when {
                                    change.changedToDown() -> {
                                        Log.d("CHANGED_UP", "CHANGED TO DOWN")
                                        hasMoved = false
                                        onUpdateCanvasStrokes(
                                            CanvasStroke(
                                                x = canvasX,
                                                y = canvasY,
                                                penType = PEN.MOVE,
                                                brushSizePx = brushPx,
                                                brushSizeNormalized = brushNormalized,
                                                colorArgb = strokeColor.toArgb()
                                            )
                                        )
                                        change.consume()
                                    }

                                    change.pressed -> {
                                        Log.d("CHANGED_UP", "CHANGED TO PRESSED")
                                        onUpdateCanvasStrokes(
                                            CanvasStroke(
                                                x = canvasX,
                                                y = canvasY,
                                                penType = PEN.DRAW,
                                                brushSizePx = brushPx,
                                                brushSizeNormalized = brushNormalized,
                                                colorArgb = strokeColor.toArgb()
                                            )
                                        )
                                        hasMoved = true
                                        change.consume()
                                    }

                                    change.changedToUp() -> {
                                        if (!hasMoved) {
                                            Log.d("CHANGED_UP", "CHANGED TO UP")
                                            onUpdateCanvasStrokes(
                                                CanvasStroke(
                                                    x = canvasX,
                                                    y = canvasY,
                                                    penType = PEN.DOT,
                                                    brushSizePx = brushPx,
                                                    brushSizeNormalized = brushNormalized,
                                                    colorArgb = strokeColor.toArgb()
                                                )
                                            )
                                        }

                                        val focus = Offset(
                                            x = canvasX / CanvasConstants.PAGE_WIDTH,
                                            y = canvasY / CanvasConstants.PAGE_HEIGHT
                                        )

                                        val offsetFractionX =
                                            -((focus.x - 0.5f) * 2f).coerceIn(-1f, 1f)
                                        val offsetFractionY =
                                            -((focus.y - 0.5f) * 2f).coerceIn(-1f, 1f)

                                        val offsetFraction = Offset(
                                            x = offsetFractionX,
                                            y = offsetFractionY
                                        )

                                        onOffsetFractionChange(
                                            offsetFraction
                                        )

                                        Log.d(
                                            "TRANSFORM_DETAILS_IN",
                                            "Focus = $focus : OffsetFraction = $offsetFraction"
                                        )

                                        change.consume()
                                    }

                                    // Detect system cancel
                                    !change.pressed && !change.changedToUp() -> {
                                        Log.d("GESTURE", "System cancelled the gesture!")
                                        onCancelCurrentStroke()
                                    }
                                }
                            }
                        }
                    }
                }
        ) {
            if (animatedProgress.value < 1f) {
                drawReplay(
                    strokes = canvasStrokes,
                    progress = animatedProgress.value
                )
            } else {
                drawNormalizedPathCached(cache)
            }
        }
    }
}

@Composable
fun rememberStrokeCache(
    strokes: List<CanvasStroke>,
    canvasWidth: Float,
    canvasHeight: Float
): StrokeCache {

    val scaleX = canvasWidth / CanvasConstants.PAGE_WIDTH
    val scaleY = canvasHeight / CanvasConstants.PAGE_HEIGHT

    return remember(strokes, scaleX, scaleY) {
        buildStrokeCache(strokes, scaleX, scaleY)
    }
}

private fun buildStrokeCache(
    strokes: List<CanvasStroke>,
    scaleX: Float,
    scaleY: Float
): StrokeCache {

    if (strokes.isEmpty()) {
        return StrokeCache(emptyList(), emptyList(), 0)
    }

    val segments = mutableListOf<PathSegment>()
    val dots = mutableListOf<Dot>()

    var currentPath = Path()
    var currentColor = strokes.first().colorArgb
    var currentBrush = strokes.first().brushSizeNormalized

    val first = strokes.first()
    currentPath.moveTo(first.x * scaleX, first.y * scaleY)

    for (i in 0 until strokes.size - 1) {

        val start = strokes[i]
        val end = strokes[i + 1]

        val startOffset = Offset(start.x * scaleX, start.y * scaleY)
        val endOffset = Offset(end.x * scaleX, end.y * scaleY)

        if (end.colorArgb != currentColor || end.brushSizeNormalized != currentBrush) {
            segments.add(
                PathSegment(
                    path = currentPath,
                    colorArgb = currentColor,
                    brushNormalized = currentBrush,
                )
            )

            currentPath = Path()
            currentColor = end.colorArgb
            currentBrush = end.brushSizeNormalized
            currentPath.moveTo(startOffset.x, startOffset.y)
        }

        when (end.penType) {

            PEN.MOVE -> {
                currentPath.moveTo(endOffset.x, endOffset.y)
            }

            PEN.DRAW -> {
                currentPath.lineTo(endOffset.x, endOffset.y)
            }

            PEN.DOT -> {
                dots.add(
                    Dot(
                        offset = endOffset,
                        progress = 1f,
                        radius = currentBrush,
                        colorArgb = currentColor
                    )
                )
            }
        }
    }

    segments.add(
        PathSegment(
            path = currentPath,
            colorArgb = currentColor,
            brushNormalized = currentBrush,
        )
    )

    return StrokeCache(segments, dots, totalSegments = strokes.size - 1)
}

private fun DrawScope.drawNormalizedPathCached(
    cache: StrokeCache,
) {
    cache.segments.forEach { segment ->
        drawPath(
            path = segment.path,
            color = Color(segment.colorArgb),
            style = Stroke(width = segment.brushNormalized * size.width)
        )
    }
    cache.dots.forEach { dot ->
        drawCircle(
            radius = dot.radius * size.width,
            center = dot.offset,
            color = Color(dot.colorArgb),
            style = Fill
        )
    }
}

private fun DrawScope.drawReplay(
    strokes: List<CanvasStroke>,
    progress: Float = 1f,
) {
    val scaleX = size.width / CanvasConstants.PAGE_WIDTH
    val scaleY = size.height / CanvasConstants.PAGE_HEIGHT

    if (strokes.isEmpty()) return

    val path = Path()
    val dotOffsets = mutableListOf<Dot>()

    val totalSegments = strokes.size - 1
    val totalProgress = progress * totalSegments

    val first = strokes.first()
    path.moveTo(first.x * scaleX, first.y * scaleY)

    var currentColor = first.colorArgb
    var currentBrush = first.brushSizeNormalized

    for (i in 0 until totalSegments) {

        val start = strokes[i]
        val end = strokes[i + 1]

        val startOffset = Offset(start.x * scaleX, start.y * scaleY)
        val endOffset = Offset(end.x * scaleX, end.y * scaleY)

        // handle color/width change
        if (end.colorArgb != currentColor || end.brushSizeNormalized != currentBrush) {
            drawPath(
                path,
                Color(currentColor),
                style = Stroke(width = (currentBrush * size.width))
            )
            path.reset()
            currentColor = end.colorArgb
            currentBrush = end.brushSizeNormalized
        }

        when (end.penType) {
            PEN.MOVE -> path.moveTo(endOffset.x, endOffset.y)

            PEN.DRAW -> {
                if (totalProgress > i + 1) {
                    path.lineTo(endOffset.x, endOffset.y)
                } else if (totalProgress > i) {
                    val segmentProgress = totalProgress - i
                    val interpolatedX =
                        startOffset.x + (endOffset.x - startOffset.x) * segmentProgress
                    val interpolatedY =
                        startOffset.y + (endOffset.y - startOffset.y) * segmentProgress
                    path.lineTo(interpolatedX, interpolatedY)
                    break
                }
            }

            PEN.DOT -> {
                if (totalProgress > i + 1) {
                    dotOffsets.add(Dot(endOffset, 1f, currentBrush, currentColor))
                } else if (totalProgress > i) {
                    val segmentProgress = totalProgress - i
                    dotOffsets.add(Dot(endOffset, segmentProgress, currentBrush, currentColor))
                    break
                }
            }
        }
    }

    // draw remaining path
    drawPath(path, Color(currentColor), style = Stroke(width = (currentBrush * size.width)))

    // draw dots
    dotOffsets.forEach { dot ->
        drawCircle(
            radius = dot.progress * ((dot.radius * size.width)),
            center = dot.offset,
            color = Color(dot.colorArgb),
            style = Fill
        )
    }
}

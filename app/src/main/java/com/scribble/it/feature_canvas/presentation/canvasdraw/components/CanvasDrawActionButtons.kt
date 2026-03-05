package com.scribble.it.feature_canvas.presentation.canvasdraw.components

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.systemGestureExclusion
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.changedToDown
import androidx.compose.ui.input.pointer.changedToUp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.scribble.it.feature_canvas.presentation.canvasdraw.ui.adaptive.metrics.CanvasDrawActionButtonsMetrics
import com.scribble.it.ui.theme.ScribbleTheme
import kotlin.math.roundToInt

@Composable
fun CanvasDrawActionButtons(
    modifier: Modifier = Modifier,
    metrics: CanvasDrawActionButtonsMetrics,
    strokeOptions: Boolean,
    showStrokeOptions: (Boolean) -> Unit,
    actionButtonsOption: Boolean,
    showActionButtonsOption: (Boolean) -> Unit,
    undoDrawing: () -> Unit,
    redoDrawing: () -> Unit,
    clearDrawing: () -> Unit
) {
    val density = LocalDensity.current
    var reveal by remember { mutableFloatStateOf(1f) }

    var handleWidth by remember { mutableStateOf(5.dp) }
    var actionButtonsWidthPx = with(density) { 66.dp.toPx().roundToInt() }

    LaunchedEffect(strokeOptions, actionButtonsOption) {
        Log.d("OPTIONS", "STROKE OPTIONS: $strokeOptions : ACTION OPTIONS: $actionButtonsOption")
        reveal = if (actionButtonsOption && !strokeOptions)
            0f
        else
            1f
    }

    val slidePx = remember(reveal) { reveal * actionButtonsWidthPx }
    val handleOffsetX by animateFloatAsState(targetValue = slidePx)

    Log.d(
        "REVEAL",
        """
        Reveal : $reveal
        Slide  : $slidePx
        ActionButtonsWidth: ${with(LocalDensity.current) {actionButtonsWidthPx.toDp()}}
        """.trimIndent()
    )

    val scrollState = rememberScrollState()
    val heightModifier = if (metrics.allowScroll) {
        Modifier
            .fillMaxHeight()
            .verticalScroll(scrollState)
    } else {
        Modifier.fillMaxHeight()
    }

    val verticalArrangement = if (metrics.allowScroll) {
        Arrangement.spacedBy(
            space = 50.dp,
            alignment = Alignment.CenterVertically
        )
    } else {
        Arrangement.SpaceEvenly
    }

    Row(
        modifier = modifier
            .offset { IntOffset(x = handleOffsetX.roundToInt(), y = 0) }
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp)
            )
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        awaitPointerEvent()
                    }
                }
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(26.dp)
                .height(70.dp)
                .systemGestureExclusion { coords ->
                    Rect(
                        0f,
                        0f,
                        coords.size.width.toFloat(),
                        coords.size.height.toFloat()
                    )
                }
                .pointerInput(actionButtonsWidthPx, strokeOptions) {
                    if(!strokeOptions) {
                        this.awaitPointerEventScope {
                            var lastX = 0f

                            while (true) {
                                val event = awaitPointerEvent()
                                val change = event.changes.firstOrNull() ?: continue

                                when {
                                    change.changedToDown() -> {
                                        handleWidth = 10.dp
                                        lastX = change.position.x
                                        change.consume()
                                    }

                                    change.pressed -> {
                                        val dx = change.position.x - lastX
                                        lastX = change.position.x

                                        val deltaFraction =
                                            dx / actionButtonsWidthPx.toFloat()

                                        reveal = (reveal + deltaFraction)
                                            .coerceIn(0f, 1f)

                                        handleWidth = 10.dp
                                        change.consume()
                                    }

                                    change.changedToUp() -> {
                                        reveal = when {
                                            reveal > 0.5f -> {
                                                showActionButtonsOption(false)
                                                1f
                                            }
                                            reveal < 0.5f ->  {
                                                showActionButtonsOption(true)
                                                0f
                                            }
                                            else -> reveal
                                        }
                                        handleWidth = 5.dp
                                    }
                                }
                            }
                        }
                    }
                },
            contentAlignment = Alignment.CenterEnd
        ) {
            Box(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .width(handleWidth)
                    .height(50.dp)
                    .background(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        shape = RoundedCornerShape(10.dp)
                    )
            )
        }

        Column(
            modifier = heightModifier
                .onGloballyPositioned {
                    actionButtonsWidthPx = it.size.width
                }
                .padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = verticalArrangement
        ) {
            CanvasDrawActionButtonsContent(
                size = metrics.buttonSize,
                radius = metrics.innerShadowRadius,
                spread = metrics.innerShadowSpread,
                offset = metrics.innerShadowOffset,
                showStrokeOptions = {
                    showStrokeOptions(true)
                },
                undoDrawing = undoDrawing,
                redoDrawing = redoDrawing,
                clearDrawing = clearDrawing,
            )
        }
    }
}

@Composable
fun CanvasDrawActionButtonsContent(
    size: Dp,
    radius: Dp,
    spread: Dp,
    offset: DpOffset,
    showStrokeOptions: () -> Unit,
    clearDrawing: () -> Unit,
    undoDrawing: () -> Unit,
    redoDrawing: () -> Unit
) {
    ActionButton(
        size = size,
        radius = radius,
        spread = spread,
        offset = offset,
        icon = Icons.Default.Draw,
        tint = MaterialTheme.colorScheme.primary,
        contentDescription = "Show strokes & colors",
        onClick = {
            showStrokeOptions()
        }
    )

    ActionButton(
        size = size,
        radius = radius,
        spread = spread,
        offset = offset,
        icon = Icons.AutoMirrored.Default.Undo,
        tint = MaterialTheme.colorScheme.secondary,
        contentDescription = "Undo draw",
        onClick = {
            undoDrawing()
        }
    )

    ActionButton(
        size = size,
        radius = radius,
        spread = spread,
        offset = offset,
        icon = Icons.AutoMirrored.Default.Redo,
        tint = MaterialTheme.colorScheme.secondary,
        contentDescription = "Redo draw",
        onClick = {
            redoDrawing()
        }
    )

    ActionButton(
        size = size,
        radius = radius,
        spread = spread,
        offset = offset,
        icon = Icons.Default.Delete,
        tint = MaterialTheme.colorScheme.tertiary,
        contentDescription = "clear draw",
        onClick = {
            clearDrawing()
        }
    )
}

@Composable
fun ActionButton(
    size: Dp,
    radius: Dp,
    spread: Dp,
    offset: DpOffset,
    icon: ImageVector,
    tint: Color,
    contentDescription: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(size)
            .aspectRatio(1f)
            .innerShadow(
                shape = RoundedCornerShape(12.dp),
                shadow = Shadow(
                    radius = radius,
                    spread = spread,
                    color = Color(0x40000000),
                    offset = offset
                )
            )
            .clip(RoundedCornerShape(12.dp))
            .clickable(
                indication = ripple(
                    bounded = true,
                    color = ScribbleTheme.scribbleColors.canvasDrawActionButtonRipple
                ),
                interactionSource = remember { MutableInteractionSource() },
            ) {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint
        )
    }
}

@Preview
@Composable
fun CanvasDrawActionButtonsPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        CanvasDrawActionButtons(
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.TopEnd),
            metrics = CanvasDrawActionButtonsMetrics(),
            strokeOptions = false,
            showStrokeOptions = {},
            actionButtonsOption = false,
            showActionButtonsOption = {},
            undoDrawing = {},
            clearDrawing = {},
            redoDrawing = {}
        )
    }
}


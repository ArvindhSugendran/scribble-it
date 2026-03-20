package com.scribble.it.feature_canvas.presentation.canvasdraw.components

import android.util.Log
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.scribble.it.feature_canvas.presentation.canvasdraw.state.ReplayState
import com.scribble.it.feature_canvas.presentation.canvasdraw.state.ViewportTransform
import com.scribble.it.feature_canvas.presentation.canvasdraw.ui.adaptive.metrics.CanvasViewportMetrics

@Composable
fun CanvasViewport(
    modifier: Modifier = Modifier,
    maxWidth: Dp,
    maxHeight: Dp,
    offsetFraction: Offset,
    replayState: ReplayState,
    metrics: CanvasViewportMetrics,
    onOffsetFractionChange: (Offset) -> Unit,
    content: @Composable (ViewportTransform) -> Unit
) {
    val density = LocalDensity.current
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    val drawActionButtonOverlayWidth = 26.dp
    val drawActionOverlayPx = with(density) { drawActionButtonOverlayWidth.toPx() }

    val viewportWidthPx = with(density) { metrics.viewportWidth.toPx() }
    val viewportHeightPx = with(density) { metrics.viewportHeight.toPx() }

    val screenWidthPx = with(density) { maxWidth.toPx() }
    val screenHeightPx = with(density) { maxHeight.toPx() }

    val maxOffsetPx = with(density) {
        Offset(
            metrics.offset.x.toPx(),
            metrics.offset.y.toPx()
        )
    }

    LaunchedEffect(replayState) {
        if (replayState != ReplayState.IDLE) {
            scale = 1f
            offset = Offset.Zero
        }
    }

    LaunchedEffect(metrics) {
        if(replayState == ReplayState.IDLE) {
            scale = metrics.scale
            offset = if (offsetFraction != Offset.Zero) {
                Offset(
                    x = offsetFraction.x * maxOffsetPx.x,
                    y = offsetFraction.y * maxOffsetPx.y
                )
            } else {
                maxOffsetPx
            }

            Log.d(
                "TRANSFORM_DETAILS_INITIAL",
                """
                Scale = $scale
                Offset = $offset
                OffsetFraction = $offsetFraction
                MaxWidth = $maxWidth
                MaxHeight = $maxHeight
                ViewPortWidth = ${metrics.viewportWidth}
                ViewportHeight = ${metrics.viewportHeight}
            """.trimIndent()
            )
        }
    }

    val transformState = rememberTransformableState { zoomChange, panChange, _ ->
        val nextScale = (scale * zoomChange).coerceIn(0.75f, metrics.maxScale)

        val scaledWidth = viewportWidthPx * nextScale
        val scaledHeight = viewportHeightPx * nextScale

        val overflowX = (scaledWidth - screenWidthPx).coerceAtLeast(0f)
        val overflowY = (scaledHeight - screenHeightPx).coerceAtLeast(0f)

        val maxX = overflowX / 2f
        val maxY = overflowY / 2f

        val coerceMaxX = if(maxX > 0) -maxX - drawActionOverlayPx else maxX

        offset = Offset(
            x = (offset.x + nextScale * panChange.x).coerceIn(coerceMaxX, maxX),
            y = (offset.y + nextScale * panChange.y).coerceIn(-maxY, maxY)
        )
        onOffsetFractionChange(
            Offset(
                x = if (maxX == 0f) 0f else offset.x / maxX,
                y = if (maxY == 0f) 0f else offset.y / maxY
            )
        )

        scale = nextScale

        Log.d(
            "TRANSFORM_DETAILS_IN",
            "Scale = $scale : Offset = $offset : OffsetFraction = ${offsetFraction}}"
        )
    }

    Box(
        modifier = modifier
            .transformable(transformState),
        contentAlignment = Alignment.Center
    ) {
        content(
            ViewportTransform(
                scale = scale,
                offsetPx = offset,
                viewportSize = DpSize(
                    metrics.viewportWidth,
                    metrics.viewportHeight
                ),
            )
        )
    }
}
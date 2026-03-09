package com.scribble.it.feature_canvas.presentation.common.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.scribble.it.feature_canvas.presentation.canvasdraw.state.PageFormat

@Composable
fun CanvasGridShimmerItem(
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
    ) {
        val cardWidth = maxWidth

        val cardWidthPx = with(LocalDensity.current) {cardWidth.toPx()}
        val gradientWidthPx = 0.2f * cardWidthPx

        val gradientTransition = rememberInfiniteTransition()

        val xCardShimmer by gradientTransition.animateFloat(
            initialValue = 0f,
            targetValue = cardWidthPx + gradientWidthPx + 100f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 1000,
                    easing = LinearEasing,
                ),
                repeatMode = RepeatMode.Restart
            )
        )

        val shimmerColors = listOf(
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
            MaterialTheme.colorScheme.surface.copy(alpha = 0.4f),
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
        )

        val brush = linearGradient(
            colors = shimmerColors,
            start = Offset(xCardShimmer - gradientWidthPx, xCardShimmer - gradientWidthPx),
            end = Offset(xCardShimmer, xCardShimmer)
        )

        Column {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(brush)
            ) {
                Spacer(
                    modifier = Modifier
                        .aspectRatio(PageFormat.A4.aspectRatio)
                )
            }

            Spacer(
                modifier = Modifier
                    .height(12.dp)
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(cardWidth / 15)
                    .clip(RoundedCornerShape(12.dp))
                    .background(brush)
            )

            Spacer(
                modifier = Modifier
                    .height(12.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                Spacer(
                    modifier = Modifier
                        .weight(1f)
                        .height(cardWidth / 15)
                        .clip(RoundedCornerShape(12.dp))
                        .background(brush)
                )
            }
        }
    }
}

@Preview
@Composable
fun CanvasGridShimmerItemPreview() {
    CanvasGridShimmerItem(
        modifier = Modifier
            .fillMaxWidth()
    )
}

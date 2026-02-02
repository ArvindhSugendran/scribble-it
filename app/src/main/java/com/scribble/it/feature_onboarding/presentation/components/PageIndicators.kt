package com.scribble.it.feature_onboarding.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun PageIndicators(
    currentPage: Int,
    totalPages: Int,
    dotSize: Dp,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(totalPages) { index ->
            val isSelected = index == currentPage

            val animatedSize by animateDpAsState(
                targetValue = if (isSelected) dotSize else dotSize * 0.7f,
                label = "dot-size"
            )

            val color by animateColorAsState(
                targetValue = if (isSelected)
                    Color.White
                else
                    Color.White.copy(
                        alpha = 0.4f
                    ),
                label = "dot-color"
            )

            Box(
                modifier = Modifier
                    .size(animatedSize)
                    .background(
                        color = color,
                        shape = CircleShape
                    )
            )
        }
    }
}
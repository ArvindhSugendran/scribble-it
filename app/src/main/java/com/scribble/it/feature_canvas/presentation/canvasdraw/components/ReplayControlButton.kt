package com.scribble.it.feature_canvas.presentation.canvasdraw.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun ReplayControlButton(
    icon: ImageVector,
    active: Boolean,
    onClick: () -> Unit
) {
    val itemSize = 50.dp

    Box(
        modifier = Modifier
            .size(itemSize)
            .clip(CircleShape)
            .background(
                if (active)
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
                else
                    MaterialTheme.colorScheme.primary
            )
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.3f),
                shape = CircleShape
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.fillMaxSize(0.5f)
        )
    }
}
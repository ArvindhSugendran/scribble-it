package com.scribble.it.feature_canvas.presentation.common.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.KeyboardDoubleArrowDown
import androidx.compose.material.icons.filled.Recycling
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.scribble.it.feature_canvas.presentation.common.ui.adaptive.metrics.CanvasAppBarMetrics

@Composable
fun CanvasAppBar(
    modifier: Modifier = Modifier,
    metrics: CanvasAppBarMetrics,
    appName: String,
    leadingIcon: (@Composable () -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier
            .padding(
                start = 16.dp,
                top = 8.dp,
                bottom = 5.dp,
                end = 10.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (leadingIcon != null) {
            leadingIcon()

            Spacer(modifier = Modifier.width(5.dp))
        }

        Text(
            text = appName,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = metrics.appNameTextStyle,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(1f)
        )

        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(metrics.iconsSpacedByPadding),
            verticalAlignment = Alignment.CenterVertically,
            content = actions
        )
    }
}

@Preview
@Composable
fun CanvasAppBarPreview() {
    CanvasAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .systemBarsPadding(),
        metrics = CanvasAppBarMetrics(
            appNameTextStyle = MaterialTheme.typography.headlineLarge,
            iconsSpacedByPadding = 12.dp
        ),
        appName = "Scribble",
        actions = {
            IconButton(
                modifier = Modifier.size(35.dp),
                onClick = { },
                enabled = true
            ) {
                Icon(
                    imageVector = Icons.Default.Recycling,
                    contentDescription = "Recycle Bin",
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }

            IconButton(
                modifier = Modifier.size(35.dp),
                onClick = { },
                enabled = true
            ) {
                Icon(
                    imageVector = Icons.Default.SwapVert,
                    contentDescription = "Sort",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            IconButton(
                modifier = Modifier.size(35.dp),
                onClick = { },
                enabled = true
            ) {
                AnimatedContent(targetState = false) { grid ->
                    Icon(
                        imageVector = if (grid)
                            Icons.Default.GridView
                        else
                            Icons.AutoMirrored.Default.ListAlt,
                        contentDescription = "Grid / List View",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun CanvasRecycleAppBarPreview() {
    CanvasAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .systemBarsPadding(),
        metrics = CanvasAppBarMetrics(
            appNameTextStyle = MaterialTheme.typography.headlineLarge,
            iconsSpacedByPadding = 12.dp
        ),
        appName = "Trash",
        leadingIcon = {
            IconButton(
                modifier = Modifier
                    .size(35.dp),
                onClick = {}
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardDoubleArrowDown,
                    contentDescription = "Close Trash Screen",
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
        },
        actions = {
            IconButton(
                modifier = Modifier.size(35.dp),
                onClick = { /* preview action */ }
            ) {
                Icon(
                    imageVector = Icons.Default.Restore,
                    contentDescription = "Restore canvas",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxSize(0.7f)
                )
            }

            // Delete button
            IconButton(
                modifier = Modifier.size(35.dp),
                onClick = { /* preview action */ }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete canvas",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxSize(0.7f)
                )
            }
        }
    )
}

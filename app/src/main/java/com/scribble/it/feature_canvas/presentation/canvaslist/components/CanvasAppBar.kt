package com.scribble.it.feature_canvas.presentation.canvaslist.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Recycling
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.scribble.it.feature_canvas.presentation.canvaslist.ui.CanvasAppBarMetrics

@Composable
fun CanvasAppBar(
    modifier: Modifier = Modifier,
    metrics: CanvasAppBarMetrics,
    appName: String,
    isGridView: Boolean,
    onRecycle: () -> Unit,
    onSort: () -> Unit,
    onToggleView: () -> Unit
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
        Text(
            text = appName,
            style = metrics.appNameTextStyle,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(1f)
        )

        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(metrics.iconsSpacedByPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                modifier = Modifier.size(35.dp),
                onClick = onRecycle
            ) {
                Icon(
                    imageVector = Icons.Default.Recycling,
                    contentDescription = "Recycle Bin",
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }

            IconButton(
                modifier = Modifier.size(35.dp),
                onClick = onSort
            ) {
                Icon(
                    imageVector = Icons.Default.SwapVert,
                    contentDescription = "Sort",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            IconButton(
                modifier = Modifier.size(35.dp),
                onClick = onToggleView
            ) {
                AnimatedContent(targetState = isGridView) { grid ->
                    Icon(
                        imageVector = if (grid)
                            Icons.Default.GridView
                        else
                            Icons.AutoMirrored.Default.ViewList,
                        contentDescription = "Grid / List View",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun CanvasTopBarPreview() {
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
        isGridView = true,
        onRecycle = {},
        onSort = {},
        onToggleView = {},
    )
}
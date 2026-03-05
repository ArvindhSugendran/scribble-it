package com.scribble.it.feature_canvas.presentation.common.components

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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CanvasDeleteBar(
    modifier: Modifier = Modifier,
    selectedCount: Int = 0,
    onCloseDeleteBar: () -> Unit,
    actions: @Composable RowScope.() -> Unit
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp,
        shadowElevation = 6.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    modifier = Modifier.size(35.dp),
                    onClick = { onCloseDeleteBar() }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Delete Bar",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.fillMaxSize(0.7f)
                    )
                }

                Spacer(modifier = Modifier.width(5.dp))

                Text(
                    text = "Selected ($selectedCount) ",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                content = actions
            )
        }
    }
}

@Preview
@Composable
fun CanvasDeleteBarPreview() {
    CanvasDeleteBar(
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxWidth(),
        selectedCount = 5,
        onCloseDeleteBar = {},
        actions = {
            IconButton(
                modifier = Modifier.size(35.dp),
                onClick = { }
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

@Preview(showBackground = true)
@Composable
fun CanvasRecycleDeleteBarPreview() {
    CanvasDeleteBar(
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxWidth(),
        selectedCount = 3, // simulate 3 selected items
        onCloseDeleteBar = { /* do nothing for preview */ },
        actions = {
            // Restore button
            IconButton(
                modifier = Modifier.size(35.dp),
                onClick = { /* preview action */ }
            ) {
                Icon(
                    imageVector = Icons.Default.Restore, // or any icon
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

            // Select All button
            IconButton(
                modifier = Modifier.size(35.dp),
                onClick = { /* preview action */ }
            ) {
                Icon(
                    imageVector = Icons.Default.SelectAll,
                    contentDescription = "Select All",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxSize(0.7f)
                )
            }
        }
    )
}

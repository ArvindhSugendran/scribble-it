package com.scribble.it.feature_canvas.presentation.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.scribble.it.feature_canvas.domain.model.canvasSummary.CanvasSummary
import com.scribble.it.feature_canvas.presentation.canvasdraw.state.PageFormat
import com.scribble.it.feature_canvas.presentation.common.ui.adaptive.metrics.CanvasItemMetrics
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CanvasGridItem(
    modifier: Modifier = Modifier,
    itemMetrics: CanvasItemMetrics,
    canvasSummary: CanvasSummary,
    isSelected: Boolean,
    isSelectedPreview: Boolean,
    onLongClicked: (Long) -> Unit,
    onClicked: (Long) -> Unit
) {

    val border = if (isSelectedPreview) {
        Modifier
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(12.dp)
            )
    } else {
        Modifier.border(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant,
            shape = RoundedCornerShape(12.dp)
        )
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .then(border)
            .combinedClickable(
                onClick = {
                    canvasSummary.id?.let { onClicked(it) }
                },
                onLongClick = {
                    canvasSummary.id?.let { onLongClicked(it) }
                }
            )
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(PageFormat.A4.aspectRatio)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(canvasSummary.thumbnailPath)
                        .build(),
                    contentDescription = "canvas-thumbnail",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )

            Text(
                text = canvasSummary.title,
                color = MaterialTheme.colorScheme.onSurface,
                style = itemMetrics.titleTextStyle,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
            )

            Text(
                text = canvasSummary.createdDate.toDateString(),
                style = itemMetrics.dateTextStyle,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier
                    .padding(horizontal = 6.dp, vertical = 3.dp)
            )
        }

        if (isSelected) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Selected",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxSize(0.25f)
                )
            }
        }
    }
}

@Preview
@Composable
fun CanvasGridItemPreview() {
    val sampleCanvas = CanvasSummary(
        id = 1,
        title = "My First Sketch",
        thumbnailPath = null,
        createdDate = System.currentTimeMillis(),
        modifiedDate = System.currentTimeMillis(),
        deletedDate = null
    )

    CanvasGridItem(
        modifier = Modifier
            .fillMaxWidth(),
        itemMetrics = CanvasItemMetrics(
            titleTextStyle = MaterialTheme.typography.titleMedium,
            dateTextStyle = MaterialTheme.typography.bodyMedium,
        ),
        isSelected = false,
        isSelectedPreview = true,
        canvasSummary = sampleCanvas,
        onLongClicked = {},
        onClicked = {}
    )
}

fun Long.toDateString(
    pattern: String = "MMM dd yyyy • hh:mm a"
): String {
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    return formatter.format(Date(this))
}
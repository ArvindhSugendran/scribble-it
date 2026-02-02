package com.scribble.it.feature_canvas.presentation.canvaslist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.scribble.it.R
import com.scribble.it.feature_canvas.domain.model.canvasSummary.CanvasSummary
import com.scribble.it.feature_canvas.presentation.canvaslist.ui.CanvasItemMetrics
import com.scribble.it.feature_canvas.presentation.canvaslist.viewmodel.CanvasTestingSummary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CanvasListItem(
    modifier: Modifier = Modifier,
    itemMetrics: CanvasItemMetrics,
    canvasSummary: CanvasTestingSummary,
    isSelected: Boolean,
    onLongClicked: (Int) -> Unit,
    onClicked: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
    ) {
        Column(
            modifier = modifier
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = RoundedCornerShape(12.dp)
                )
                .combinedClickable(
                    onClick = {
                        canvasSummary.id?.let { onClicked(it) }
                    },
                    onLongClick = {
                        canvasSummary.id?.let { onLongClicked(it) }
                    }
                ),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(canvasSummary.thumbnailPath ?: R.drawable.reddeadblue)
                        .placeholder(R.drawable.reddeadblue)
                        .build(),
                    contentDescription = "canvas-thumbnail",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                if (canvasSummary.deletedAt == null) {
                    Text(
                        text = "RECYCLED",
                        color = Color.White,
                        style = itemMetrics.badgeTextStyle,
                        modifier = Modifier
                            .wrapContentSize()
                            .align(Alignment.TopEnd)
                            .padding(horizontal = 5.dp, vertical = 4.dp)
                            .clip(RoundedCornerShape(topEnd = 4.dp, bottomStart = 4.dp))
                            .background(Color.Red)
                            .padding(
                                horizontal = itemMetrics.horizontalPadding,
                                vertical = itemMetrics.verticalPadding
                            )
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = Color.White
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
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Selected",
                    tint = Color.White,
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
fun CanvasListItemPreview() {
    val sampleCanvas = CanvasTestingSummary(
        id = 1,
        title = "My First Sketch",
        thumbnailPath = null,
        createdDate = System.currentTimeMillis(),
        modifiedDate = System.currentTimeMillis(),
        deletedAt = null
    )

    CanvasListItem(
        modifier = Modifier
            .fillMaxWidth(),
        itemMetrics = CanvasItemMetrics(
            titleTextStyle = MaterialTheme.typography.titleMedium,
            dateTextStyle = MaterialTheme.typography.bodyMedium,
            badgeTextStyle = MaterialTheme.typography.labelMedium,
            horizontalPadding = 2.dp,
            verticalPadding = 1.dp
        ),
        isSelected = false,
        canvasSummary = sampleCanvas,
        onLongClicked = {},
        onClicked = {}
    )
}

fun Long.toDateString(
    pattern: String = "MMM dd, yyyy hh:mm a"
): String {
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    return formatter.format(Date(this))
}
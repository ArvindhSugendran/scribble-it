package com.scribble.it.feature_canvas.presentation.canvaslist.components.previewPane

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.scribble.it.ui.theme.ScribbleTheme

@Composable
fun PreviewCanvasAppBar(
    modifier: Modifier = Modifier,
    title: String,
    closePreview: () -> Unit,
    editPreview: () -> Unit
) {
    Row(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PreviewActionButton(
            size = 50.dp,
            radius = 3.dp,
            spread = 0.dp,
            offset = DpOffset(x = 1.dp, y = 2.dp),
            icon = Icons.Default.Close,
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = "Close Preview",
            onClick = closePreview
        )

        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge.copy(
                color =  MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold
            ),
            textAlign = TextAlign.Center,
        )

        PreviewActionButton(
            size = 50.dp,
            radius = 3.dp,
            spread = 0.dp,
            offset = DpOffset(x = 1.dp, y = 2.dp),
            icon = Icons.Default.Draw,
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = "Edit Preview",
            onClick = editPreview
        )
    }
}

@Composable
fun PreviewActionButton(
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
                shape = CircleShape,
                shadow = Shadow(
                    radius = radius,
                    spread = spread,
                    color = MaterialTheme.colorScheme.onSurface,
                    offset = offset
                )
            )
            .clip(CircleShape)
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
fun PreviewCanvasAppBarPreview() {
    PreviewCanvasAppBar(
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxWidth(),
        title = "LA LA LA!!!",
        closePreview = {},
        editPreview = {}
    )
}
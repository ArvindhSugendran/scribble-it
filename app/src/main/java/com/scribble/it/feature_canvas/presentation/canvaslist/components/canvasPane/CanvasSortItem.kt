package com.scribble.it.feature_canvas.presentation.canvaslist.components.canvasPane

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

@Composable
fun CanvasSortItem(
    modifier: Modifier = Modifier,
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                color = if (selected)
                    MaterialTheme.colorScheme.primaryContainer
                else
                    Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .innerShadow(
                shape = RoundedCornerShape(12.dp),
                shadow = Shadow(
                    radius = 2.dp,
                    spread = 1.dp,
                    color = Color(0x40000000),
                    offset = DpOffset(x = 1.dp, y = 1.dp)
                )
            )
            .clickable {
                onClick()
            }
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(
                color = if (selected)
                    MaterialTheme.colorScheme.onPrimaryContainer
                else
                    MaterialTheme.colorScheme.onSurface
            ),
            fontWeight = FontWeight.Bold

        )
    }
}

@Preview
@Composable
fun CanvasSortItemPreview() {
    CanvasSortItem(
        modifier = Modifier.requiredWidth(200.dp),
        text = "CREATED_AT",
        selected = true,
        onClick = {}
    )
}
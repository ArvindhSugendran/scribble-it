package com.scribble.it.feature_canvas.presentation.canvasdraw.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Animation
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.scribble.it.feature_canvas.presentation.canvasdraw.ui.adaptive.metrics.CanvasDrawAppBarMetrics
import com.scribble.it.ui.theme.ScribbleTheme

@Composable
fun CanvasDrawAppBar(
    modifier: Modifier = Modifier,
    metrics: CanvasDrawAppBarMetrics,
    title: TextFieldValue,
    drawingEnabled: Boolean,
    onTitleChange: (TextFieldValue) -> Unit,
    onDrawingEnable: () -> Unit,
    onBackPressed: () -> Unit,
    animate: () -> Unit
) {
    Surface(
        modifier = modifier
            .dropShadow(
                shape = RectangleShape,
                shadow = Shadow(
                    radius = 5.dp,
                    spread = 0.dp,
                    color = Color(0x40000000),
                    offset = DpOffset(x = 0.dp, y = 5.dp)
                )
            ),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp,
    ) {
        Row(
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    top = 12.dp,
                    bottom = 12.dp,
                    end = 10.dp)
            ,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    modifier = Modifier
                        .size(35.dp),
                    onClick = {
                        onBackPressed()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = "Close Draw Screen",
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                BasicTextField(
                    value = title,
                    onValueChange = onTitleChange,
                    singleLine = true,
                    textStyle = MaterialTheme.typography.headlineMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    decorationBox = { innerTextField ->
                        if (title.text.isEmpty()) {
                            Text(
                                text = "Title",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = FontWeight.Normal
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        innerTextField()
                    },
                    modifier = Modifier
                        .weight(1f)
                )
            }

            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.spacedBy(metrics.iconsSpacedByPadding),
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(
                    modifier = Modifier.size(35.dp),
                    onClick = {
                        animate()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Animation,
                        contentDescription = "Animate canvas",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                
                IconButton(
                    modifier = Modifier
                        .background(
                            color = if(drawingEnabled) ScribbleTheme.scribbleColors.canvasDrawIconBackground else Color.Transparent,
                            shape = CircleShape
                        )
                        .size(35.dp),
                    onClick = { onDrawingEnable() }
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Draw enable / disable",
                        modifier = Modifier,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewCanvasDrawTopBar() {
    CanvasDrawAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding(),
        metrics = CanvasDrawAppBarMetrics(
            iconsSpacedByPadding = 12.dp,
            iconVisibility = true
        ),
        title = TextFieldValue(""),
        drawingEnabled = false,
        onTitleChange = {},
        onDrawingEnable = {},
        onBackPressed = {},
        animate = {}
    )
}
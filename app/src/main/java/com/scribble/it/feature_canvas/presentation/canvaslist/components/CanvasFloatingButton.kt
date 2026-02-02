package com.scribble.it.feature_canvas.presentation.canvaslist.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.scribble.it.feature_canvas.presentation.canvaslist.ui.CanvasFloatingButtonMetrics

@Composable
fun CanvasFloatingButton(
    modifier: Modifier = Modifier,
    metrics: CanvasFloatingButtonMetrics,
    isFabExpanded: Boolean = true
) {
    ExtendedFloatingActionButton(
        onClick = {

        },
        modifier = modifier,
        expanded = isFabExpanded,
        shape = RoundedCornerShape(12.dp),
        icon = {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Create Canvas"
            )
        },
        text = {
            Text(
                text = metrics.buttonText,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        },
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 6.dp,
            pressedElevation = 10.dp,
            focusedElevation = 8.dp,
            hoveredElevation = 8.dp
        )
    )
}

@Preview
@Composable
fun CanvasFloatingButtonPreview() {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        CanvasFloatingButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 16.dp)
                .navigationBarsPadding(),
            metrics = CanvasFloatingButtonMetrics()
        )
    }
}
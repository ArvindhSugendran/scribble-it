package com.scribble.it.feature_canvas.presentation.canvaslist.ui

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp

data class CanvasItemMetrics(
    val titleTextStyle: TextStyle,
    val dateTextStyle: TextStyle,
    val badgeTextStyle: TextStyle,
    val horizontalPadding: Dp,
    val verticalPadding: Dp,
)

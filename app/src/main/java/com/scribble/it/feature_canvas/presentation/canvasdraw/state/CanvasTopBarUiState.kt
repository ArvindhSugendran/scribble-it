package com.scribble.it.feature_canvas.presentation.canvasdraw.state

import androidx.compose.ui.text.input.TextFieldValue

data class CanvasTopBarUiState(
    val canvasTitle: TextFieldValue = TextFieldValue(""),
    val isTitleFocused: Boolean = false,
    val drawingEnabled: Boolean = false
)

package com.scribble.it.feature_canvas.presentation.canvasdraw.state

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import com.scribble.it.feature_canvas.domain.model.canvas.CanvasDrawing
import com.scribble.it.feature_canvas.domain.model.stroke.CanvasStroke

data class CanvasDrawUiState(
    val canvasDrawing: CanvasDrawing,
    val redoStrokes: List<List<CanvasStroke>> = emptyList(),
    val viewportUiState: ViewportUiState = ViewportUiState(),
    val canvasTitle: TextFieldValue = TextFieldValue(""),
    val isTitleFocused: Boolean = false,
    val drawingEnabled: Boolean = false,
    val showStrokeOptions: Boolean = false,
    val showActionButtonsOption: Boolean = false,
    val strokeColor: Color = Color.Black,
    val brushSize: BrushSize = BrushSize.VERY_THIN
)

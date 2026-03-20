package com.scribble.it.feature_canvas.presentation.canvasdraw.state

import androidx.compose.ui.graphics.Color
import com.scribble.it.feature_canvas.domain.model.canvas.CanvasDrawing
import com.scribble.it.feature_canvas.domain.model.stroke.CanvasStroke

data class CanvasContentState(
    val canvasDrawing: CanvasDrawing,
    val strokeColor: Color = Color.Black,
    val brushSize: BrushSize = BrushSize.VERY_THIN,
    val redoStrokes: List<List<CanvasStroke>> = emptyList()
)

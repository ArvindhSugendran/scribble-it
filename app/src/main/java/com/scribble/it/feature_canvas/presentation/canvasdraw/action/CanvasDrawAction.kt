package com.scribble.it.feature_canvas.presentation.canvasdraw.action

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import com.scribble.it.feature_canvas.domain.model.stroke.CanvasStroke
import com.scribble.it.feature_canvas.presentation.canvasdraw.state.BrushSize
import com.scribble.it.feature_canvas.presentation.canvasdraw.state.ReplayState

sealed class CanvasDrawAction {
    data class OnTitleChange(val title: TextFieldValue): CanvasDrawAction()
    data class ShowStrokeOptions(val showStrokeOptions: Boolean): CanvasDrawAction()
    data class ShowActionButtonsOption(val showActionButtonsOption: Boolean): CanvasDrawAction()

    data object EnableDrawing: CanvasDrawAction()

    data class ChangeBrushSize(val brushSize: BrushSize): CanvasDrawAction()
    data class ChangePageColor(val pageColor: Color): CanvasDrawAction()
    data class ChangeStrokeColor(val strokeColor: Color): CanvasDrawAction()

    data class UpdateOffsetFraction(val offset: Offset): CanvasDrawAction()
    data class UpdateStrokes(val canvasStroke: CanvasStroke) : CanvasDrawAction()
    data object CancelLastStroke: CanvasDrawAction()

    data object UndoDrawing: CanvasDrawAction()
    data object RedoDrawing: CanvasDrawAction()
    data object ClearDrawing: CanvasDrawAction()

    data object OnBackPressed: CanvasDrawAction()

    data class Replay(val replayState: ReplayState): CanvasDrawAction()
    data object RestartReplay: CanvasDrawAction()
}
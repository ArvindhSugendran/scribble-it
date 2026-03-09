package com.scribble.it.feature_canvas.presentation.canvasdraw.viewmodel

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.scribble.it.feature_canvas.domain.error.CanvasError
import com.scribble.it.feature_canvas.domain.model.canvas.CanvasDrawing
import com.scribble.it.feature_canvas.domain.model.operation.DeleteRequest
import com.scribble.it.feature_canvas.domain.model.stroke.CanvasStroke
import com.scribble.it.feature_canvas.domain.model.stroke.PEN
import com.scribble.it.feature_canvas.domain.result.Result
import com.scribble.it.feature_canvas.domain.usecase.DeleteCanvasesUseCase
import com.scribble.it.feature_canvas.domain.usecase.GenerateScribbleTitleUseCase
import com.scribble.it.feature_canvas.domain.usecase.GetCanvasByIdUseCase
import com.scribble.it.feature_canvas.domain.usecase.SaveCanvasUseCase
import com.scribble.it.feature_canvas.presentation.canvasdraw.action.CanvasDrawAction
import com.scribble.it.feature_canvas.presentation.canvasdraw.event.CanvasDrawEvent
import com.scribble.it.feature_canvas.presentation.canvasdraw.navigation.CanvasDrawRoute
import com.scribble.it.feature_canvas.presentation.canvasdraw.state.CanvasDrawUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

@HiltViewModel
class CanvasDrawViewModel @Inject constructor(
    private val saveCanvasUseCase: SaveCanvasUseCase,
    private val deleteCanvasesUseCase: DeleteCanvasesUseCase,
    private val getCanvasByIdUseCase: GetCanvasByIdUseCase,
    private val generateScribbleTitleUseCase: GenerateScribbleTitleUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val strokeColors = listOf(
        Color(0xFF000000), Color(0xFFFFFFFF), Color(0xFF444444),
        Color(0xFF888888), Color(0xFFCCCCCC), Color(0xFFFF0000),
        Color(0xFF00FF00), Color(0xFF0000FF), Color(0xFFFFFF00),
        Color(0xFFFF00FF), Color(0xFF00FFFF), Color(0xFFFF5722),
        Color(0xFFFF9800), Color(0xFFFFC107), Color(0xFF795548),
        Color(0xFF3F51B5), Color(0xFF2196F3), Color(0xFF009688),
        Color(0xFF4CAF50), Color(0xFFFFCDD2), Color(0xFFBBDEFB),
        Color(0xFFC8E6C9), Color(0xFFFFF9C4), Color(0xFFE1BEE7)
    )

    private val _canvasDrawUiState =
        MutableStateFlow(CanvasDrawUiState(canvasDrawing = CanvasDrawing()))
    val canvasDrawUiState: StateFlow<CanvasDrawUiState> = _canvasDrawUiState.asStateFlow()

    private val eventChannel = Channel<CanvasDrawEvent>(Channel.BUFFERED)
    val eventsFlow: Flow<CanvasDrawEvent> = eventChannel.receiveAsFlow()

    private var previousDrawingEnabled: Boolean? = null
    private var saveJob: Job? = null
    private val deBounce = 1000L

    private val saveMutex = Mutex()

    // --- Idempotent Save Tracking ---
    private var contentVersion = 0
    private var lastSavedVersion = -1

    init {
        val scribbleId = savedStateHandle.toRoute<CanvasDrawRoute>().id
        scribbleId?.let {
            viewModelScope.launch {
                getCanvasByIdUseCase(canvasId = scribbleId)
                    .collectLatest { result: Result<CanvasDrawing, CanvasError> ->
                        when (result) {
                            is Result.Error -> Log.d(
                                "CanvasById",
                                result.error.toString()
                            )

                            is Result.Loading -> Log.d("CanvasById", "LOADING")
                            is Result.Success -> {
                                Log.d("CanvasById", "Result: ${result.data}")

                                val canvasDrawing = result.data
                                _canvasDrawUiState.update {
                                    it.copy(
                                        canvasDrawing = canvasDrawing,
                                        canvasTitle = TextFieldValue(canvasDrawing.title),
                                        strokeColor = if (canvasDrawing.pageColor == Color.Black.toArgb()) Color.White else Color.Black
                                    )
                                }
                            }
                        }
                    }
            }
        }
    }

    fun viewAction(action: CanvasDrawAction) {
        when (action) {

            is CanvasDrawAction.ChangeBrushSize -> {
                _canvasDrawUiState.update {
                    it.copy(
                        brushSize = action.brushSize,
                    )
                }
            }

            is CanvasDrawAction.ChangePageColor -> {
                val (fixedStrokes, fixedBrushColor) = onPageColorChanged(action.pageColor)
                _canvasDrawUiState.update {
                    it.copy(
                        canvasDrawing = it.canvasDrawing.copy(
                            pageColor = action.pageColor.toArgb(),
                            canvasStrokes = fixedStrokes
                        ),
                        strokeColor = fixedBrushColor,
                    )
                }
                markDirty()
            }

            is CanvasDrawAction.ChangeStrokeColor -> {
                _canvasDrawUiState.update {
                    it.copy(
                        strokeColor = action.strokeColor,
                    )
                }
            }

            is CanvasDrawAction.EnableDrawing -> _canvasDrawUiState.update {
                it.copy(
                    drawingEnabled = !it.drawingEnabled,
                    showActionButtonsOption = false
                )
            }

            is CanvasDrawAction.OnTitleChange -> {
                markDirty()
                _canvasDrawUiState.update { it.copy(canvasTitle = action.title) }
            }

            is CanvasDrawAction.ShowStrokeOptions -> _canvasDrawUiState.update {
                it.copy(
                    showStrokeOptions = action.showStrokeOptions
                )
            }

            is CanvasDrawAction.ShowActionButtonsOption -> _canvasDrawUiState.update { state ->
                if (action.showActionButtonsOption) {
                    previousDrawingEnabled = state.drawingEnabled
                    state.copy(showActionButtonsOption = true, drawingEnabled = false)
                } else {
                    val restoredState = state.copy(
                        showActionButtonsOption = false,
                        drawingEnabled = previousDrawingEnabled ?: state.drawingEnabled
                    )
                    previousDrawingEnabled = null
                    restoredState
                }
            }

            is CanvasDrawAction.UpdateOffsetFraction -> _canvasDrawUiState.update {
                it.copy(viewportUiState = it.viewportUiState.copy(offsetFraction = action.offset))
            }

            is CanvasDrawAction.UpdateStrokes -> {
                _canvasDrawUiState.update {
                    it.copy(
                        canvasDrawing = it.canvasDrawing.copy(canvasStrokes = it.canvasDrawing.canvasStrokes + action.canvasStroke),
                        redoStrokes = emptyList()
                    )
                }

                markDirty()
                scheduleDebouncedSave()
            }

            is CanvasDrawAction.CancelLastStroke -> {
                val canvasStrokes = _canvasDrawUiState.value.canvasDrawing.canvasStrokes
                val lastMovedStrokeIndex = canvasStrokes.indexOfLast { it.penType == PEN.MOVE }

                val newStrokes = canvasStrokes.subList(0, lastMovedStrokeIndex)
                _canvasDrawUiState.update {
                    it.copy(
                        canvasDrawing = it.canvasDrawing.copy(
                            canvasStrokes = newStrokes
                        )
                    )
                }
            }

            is CanvasDrawAction.UndoDrawing -> {
                val strokes = _canvasDrawUiState.value.canvasDrawing.canvasStrokes
                val grouped = groupStrokes(strokes)
                if (grouped.isEmpty()) return
                val lastStroke = listOf(grouped.last())
                val remaining = grouped.dropLast(1).flatten()

                _canvasDrawUiState.update {
                    it.copy(
                        canvasDrawing = it.canvasDrawing.copy(canvasStrokes = remaining),
                        redoStrokes = it.redoStrokes + lastStroke
                    )
                }

                markDirty()
                scheduleDebouncedSave()
            }

            is CanvasDrawAction.RedoDrawing -> {
                val redoStack = _canvasDrawUiState.value.redoStrokes
                if (redoStack.isEmpty()) return
                val strokeToRestore = redoStack.last()
                val remainingRedo = redoStack.dropLast(1)

                _canvasDrawUiState.update {
                    it.copy(
                        canvasDrawing = it.canvasDrawing.copy(canvasStrokes = it.canvasDrawing.canvasStrokes + strokeToRestore),
                        redoStrokes = remainingRedo
                    )
                }

                markDirty()
                scheduleDebouncedSave()
            }

            is CanvasDrawAction.ClearDrawing -> {
                _canvasDrawUiState.update {
                    it.copy(canvasDrawing = it.canvasDrawing.copy(canvasStrokes = emptyList()))
                }

                markDirty()
                scheduleDebouncedSave()
            }

            // Back press: immediate navigation + save
            is CanvasDrawAction.OnBackPressed -> {
                viewModelScope.launch {
                    saveCanvasImmediately()
                    eventChannel.send(CanvasDrawEvent.NavigateBack)
                }
            }
        }
    }

    /** ---------------- DEBOUNCED SAVE ---------------- */
    private fun scheduleDebouncedSave() {
        saveJob?.cancel()
        saveJob = viewModelScope.launch {
            delay(deBounce)
            saveCanvas()
        }
    }

    private suspend fun saveCanvasImmediately() {
        saveJob?.cancel()
        saveCanvas()
    }

    private suspend fun saveCanvas() = saveMutex.withLock {

        if (contentVersion == lastSavedVersion) {
            Log.d("CanvasSave", "Skipped — no changes")
            return
        }

        val state = _canvasDrawUiState.value
        val canvas = state.canvasDrawing

        Log.d("CanvasSave", "CanvasStrokes: ${canvas.canvasStrokes}")

        if (canvas.canvasStrokes.isEmpty()) {
            canvas.id?.let {
                canvas.thumbnailPath?.let {
                    deleteCanvasesUseCase(DeleteRequest.Canvases(listOf(canvas))).collectLatest { result ->
                        when (result) {
                            is Result.Error -> Log.d("CanvasDelete", result.error.toString())
                            is Result.Loading -> Log.d("CanvasDelete", "LOADING")
                            is Result.Success -> Log.d("CanvasDelete", "Deleted Successfully")
                        }
                    }
                }
            }

            lastSavedVersion = contentVersion
            return
        }

        val inputTitle = state.canvasTitle.text

        val (finalTitle, autoIndex) = when {
            canvas.id == null && inputTitle.isBlank() -> {
                val (title, autoTitleIndex) = generateScribbleTitleUseCase()
                title to autoTitleIndex
            }
            canvas.autoTitleIndex != null -> {
                if (inputTitle.isBlank()) {
                    canvas.title to canvas.autoTitleIndex
                } else if (inputTitle == canvas.title) {
                    canvas.title to canvas.autoTitleIndex
                } else {
                    inputTitle to null
                }
            }
            else -> {
                if (inputTitle.isBlank())
                    canvas.title to null
                else
                    inputTitle to null
            }
        }

        Log.d("CanvasSave", "AutoIndex: $autoIndex")

        val updatedCanvas = canvas.copy(title = finalTitle, autoTitleIndex = autoIndex)

        saveCanvasUseCase(updatedCanvas).collectLatest { result ->
            when (result) {
                is Result.Error -> Log.d("CanvasSave", result.error.toString())
                is Result.Loading -> Log.d("CanvasSave", "LOADING")
                is Result.Success -> {
                    lastSavedVersion = contentVersion

                    Log.d("CanvasSave", "Result: ${result.data}")
                    _canvasDrawUiState.update { state ->
                        state.copy(
                            canvasDrawing = state.canvasDrawing.copy(
                                id = result.data.id,
                                title = result.data.title,
                                autoTitleIndex = result.data.autoIndex,
                                thumbnailPath = result.data.thumbnailPath
                            )
                        )
                    }
                }
            }
        }
    }

    /** ---------------- STROKE HELPERS ---------------- */
    private fun groupStrokes(strokes: List<CanvasStroke>): List<List<CanvasStroke>> {
        val result = mutableListOf<List<CanvasStroke>>()
        var current = mutableListOf<CanvasStroke>()
        strokes.forEach { stroke ->
            if (stroke.penType == PEN.MOVE && current.isNotEmpty()) {
                result.add(current)
                current = mutableListOf()
            }
            current.add(stroke)
        }
        if (current.isNotEmpty()) result.add(current)
        return result
    }

    private fun onPageColorChanged(newPageColor: Color): Pair<List<CanvasStroke>, Color> {
        val fixStrokes = _canvasDrawUiState.value.canvasDrawing.canvasStrokes.map { stroke ->
            when {
                newPageColor == Color.Black && stroke.colorArgb == Color.Black.toArgb() -> stroke.copy(
                    colorArgb = Color.White.toArgb()
                )

                newPageColor == Color.White && stroke.colorArgb == Color.White.toArgb() -> stroke.copy(
                    colorArgb = Color.Black.toArgb()
                )

                else -> stroke
            }
        }
        val fixBrushColor = if (_canvasDrawUiState.value.strokeColor == newPageColor) {
            if (newPageColor == Color.Black) Color.White else Color.Black
        } else _canvasDrawUiState.value.strokeColor

        return fixStrokes to fixBrushColor
    }

    private fun markDirty() {
        Log.d("CanvasSave", "MARK DIRTY")
        contentVersion++
    }
}
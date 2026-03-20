package com.scribble.it.feature_canvas.presentation.canvasdraw.state

data class CanvasDrawUiState(
    val canvasContentState: CanvasContentState,
    val viewportUiState: ViewportUiState = ViewportUiState(),
    val topBarUiState: CanvasTopBarUiState = CanvasTopBarUiState(),
    val drawActionButtonsUiState: CanvasDrawActionButtonsUiState = CanvasDrawActionButtonsUiState(),
    val replayUiState: ReplayUiState = ReplayUiState(),
)

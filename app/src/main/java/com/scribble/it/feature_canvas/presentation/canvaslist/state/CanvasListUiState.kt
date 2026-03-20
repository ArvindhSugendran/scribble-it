package com.scribble.it.feature_canvas.presentation.canvaslist.state

data class CanvasListUiState(
    val pane: PaneUiState = PaneUiState(),
    val topBar: TopBarUiState = TopBarUiState(),
    val list: ListUiState = ListUiState(),
    val fab: FabUiState = FabUiState(),
    val dialog: DialogUiState = DialogUiState()
)
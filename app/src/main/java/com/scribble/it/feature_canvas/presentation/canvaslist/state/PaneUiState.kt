package com.scribble.it.feature_canvas.presentation.canvaslist.state

data class PaneUiState(
    val paneMode: PaneMode = PaneMode.SinglePane,
    val paneReveal: Float = 0.5f,
    val selectedScribbleId: Long = -1
)

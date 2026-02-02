package com.scribble.it.feature_canvas.presentation.canvaslist.state

import androidx.compose.ui.text.input.TextFieldValue
import com.scribble.it.feature_canvas.presentation.canvaslist.viewmodel.CanvasTestingSummary

data class CanvasListUiState(
    val topBarMode: TopBarMode = TopBarMode.DEFAULT,
    val canvasList: List<CanvasTestingSummary> = emptyList(),
    val searchResults: List<CanvasTestingSummary> = emptyList(),
    val selectedIds: Set<Int> = emptySet(),
    val isInitialLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val isFabExpanded: Boolean = true,
    val isGrid: Boolean = true,
    val query: TextFieldValue = TextFieldValue(""),
)
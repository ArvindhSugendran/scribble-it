package com.scribble.it.feature_canvas.presentation.canvaslist.state

import androidx.compose.ui.text.input.TextFieldValue
import com.scribble.it.feature_canvas.domain.model.operation.SortOption
import com.scribble.it.feature_canvas.presentation.common.state.TopBarMode

data class TopBarUiState(
    val topBarMode: TopBarMode = TopBarMode.DEFAULT,
    val query: TextFieldValue = TextFieldValue(""),
    val sortOption: SortOption = SortOption.CREATED_DATE_DESC,
    val sortOptions: List<SortOption> = SortOption.entries.toList()
)

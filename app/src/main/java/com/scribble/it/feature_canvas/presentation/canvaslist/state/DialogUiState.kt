package com.scribble.it.feature_canvas.presentation.canvaslist.state

import com.scribble.it.feature_canvas.presentation.common.dialog.state.ConfirmationDialogState

data class DialogUiState(
    val confirmationDialog: ConfirmationDialogState = ConfirmationDialogState()
)

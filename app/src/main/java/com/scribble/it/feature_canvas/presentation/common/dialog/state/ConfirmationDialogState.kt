package com.scribble.it.feature_canvas.presentation.common.dialog.state

import com.scribble.it.feature_canvas.presentation.common.action.BulkRecycleActionType

data class ConfirmationDialogState(
    val isVisible: Boolean = false,
    val title: String = "",
    val message: String = "",
    val confirmText: String = "Confirm",
    val dismissText: String = "Cancel",
    val actionType: BulkRecycleActionType? = null,
    val targetIds: Set<Long> = emptySet()
)

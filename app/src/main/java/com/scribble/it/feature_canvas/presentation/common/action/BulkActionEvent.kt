package com.scribble.it.feature_canvas.presentation.common.action

sealed interface BulkActionEvent {
    data class Request(
        val type: BulkRecycleActionType,
        val targetIds: Set<Long>
    ) : BulkActionEvent

    data object Confirm : BulkActionEvent
    data object Dismiss : BulkActionEvent
}
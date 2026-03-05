package com.scribble.it.feature_canvas.presentation.common.action

data class CanvasItemInteraction(
    val clickType: CanvasItemClickType,
    val selectedId: Long,
    val availableItemIds: Set<Long> = emptySet()
)

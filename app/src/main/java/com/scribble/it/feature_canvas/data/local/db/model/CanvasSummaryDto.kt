package com.scribble.it.feature_canvas.data.local.db.model

data class CanvasSummaryDto(
    val id: Long?,
    val title: String,
    val thumbnailPath: String?,
    val createdDate: Long,
    val modifiedDate: Long,
    val deletedDate: Long?
)

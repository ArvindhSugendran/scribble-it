package com.scribble.it.feature_canvas.data.local.db.model

data class CanvasSummaryEntity(
    val id: Int?,
    val title: String,
    val thumbnailPath: String?,
    val createdDate: Long,
    val modifiedDate: Long,
    val deletedAt: Long?
)

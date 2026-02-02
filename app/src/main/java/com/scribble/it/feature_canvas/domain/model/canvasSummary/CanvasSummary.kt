package com.scribble.it.feature_canvas.domain.model.canvasSummary

data class CanvasSummary(
    val id: Int? = null,
    val title: String,
    val thumbnailPath: String? = null,
    val createdDate: Long,
    val modifiedDate: Long,
    val deletedAt: Long?
)

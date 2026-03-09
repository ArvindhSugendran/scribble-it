package com.scribble.it.feature_canvas.domain.model.usecaseResult

data class SaveUsesCaseResult(
    val id: Long,
    val autoIndex: Int? = null,
    val title: String,
    val thumbnailPath: String
)

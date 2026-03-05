package com.scribble.it.feature_canvas.domain.model.operation

enum class SortOption {
    TITLE_ASC,
    TITLE_DESC,
    CREATED_DATE_ASC,
    CREATED_DATE_DESC,
    MODIFIED_DATE_ASC,
    MODIFIED_DATE_DESC
}

fun SortOption.displayName(): String = when (this) {
    SortOption.TITLE_ASC -> "Title ↑"
    SortOption.TITLE_DESC -> "Title ↓"
    SortOption.CREATED_DATE_ASC -> "Created ↑"
    SortOption.CREATED_DATE_DESC -> "Created ↓"
    SortOption.MODIFIED_DATE_ASC -> "Modified ↑"
    SortOption.MODIFIED_DATE_DESC -> "Modified ↓"
}
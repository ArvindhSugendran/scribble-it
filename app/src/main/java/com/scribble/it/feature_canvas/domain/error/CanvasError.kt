package com.scribble.it.feature_canvas.domain.error

enum class CanvasError : Error {
    INVALID_ID,
    INVALID_TIME_STAMP_LIMIT,
    INVALID_CANVAS_ID_LIST,
    INVALID_TITLE,
    INVALID_CANVAS_LIST,
    CANVAS_NOT_FOUND,
    DATABASE_ERROR,
    UNKNOWN_ERROR
}
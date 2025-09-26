package com.scribble.it.feature_canvas.core.graphics.error

sealed class ThumbnailError(cause: Throwable? = null): Exception(cause) {
    data class GenerateError(
        val originalException: Throwable? = null,
        val errorType: GenerateErrorType
    ): ThumbnailError(originalException)

    data class SaveError(
        val originalException: Throwable? = null,
        val errorType: SaveErrorType
    ): ThumbnailError(originalException)

    data object EmptyDataError: ThumbnailError() {
        private fun readResolve(): Any = EmptyDataError
    }
}
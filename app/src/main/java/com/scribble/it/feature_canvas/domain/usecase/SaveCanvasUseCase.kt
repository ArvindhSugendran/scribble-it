package com.scribble.it.feature_canvas.domain.usecase

import android.database.SQLException
import com.scribble.it.feature_canvas.domain.model.canvas.CanvasDrawing
import com.scribble.it.feature_canvas.domain.repository.CanvasRepository
import com.scribble.it.feature_canvas.domain.error.CanvasError
import com.scribble.it.feature_canvas.domain.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SaveCanvasUseCase(
    private val canvasRepository: CanvasRepository
) {
    operator fun invoke(canvasDrawing: CanvasDrawing): Flow<Result<Unit, CanvasError>> = flow {
        emit(Result.Loading)

        if (canvasDrawing.title.isBlank()) {
            emit(Result.Error(CanvasError.INVALID_TITLE))
            return@flow
        }

        try {
            //Thumbnail Generation will be done here (but later)

            val now = System.currentTimeMillis()
            val canvasWithTimeStampsAndThumbnail = canvasDrawing.copy(
                createdDate = canvasDrawing.id?.let { canvasDrawing.createdDate } ?: now,
                modifiedDate = now,
            )

            canvasRepository.upsertCanvas(canvasWithTimeStampsAndThumbnail)
            emit(Result.Success(Unit))
        } catch (e: SQLException) {
            emit(Result.Error(CanvasError.DATABASE_ERROR))
        } catch (e: Exception) {
            emit(Result.Error(CanvasError.UNKNOWN_ERROR))
        }
    }
}
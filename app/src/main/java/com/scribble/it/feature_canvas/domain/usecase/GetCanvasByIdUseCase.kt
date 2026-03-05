package com.scribble.it.feature_canvas.domain.usecase

import android.database.SQLException
import com.scribble.it.feature_canvas.domain.model.canvas.CanvasDrawing
import com.scribble.it.feature_canvas.domain.repository.CanvasRepository
import com.scribble.it.feature_canvas.domain.error.CanvasError
import com.scribble.it.feature_canvas.domain.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCanvasByIdUseCase @Inject constructor(
    private val canvasRepository: CanvasRepository
) {
     operator fun invoke(canvasId: Long): Flow<Result<CanvasDrawing, CanvasError>> = flow {
        if (canvasId <= 0) {
            emit(Result.Error(CanvasError.INVALID_ID))
        }

        try {
            val canvasDrawing = canvasRepository.getCanvasById(canvasId)
            canvasDrawing
                ?.let {
                    emit(Result.Success(canvasDrawing))
                }
                ?: emit(Result.Error(CanvasError.CANVAS_NOT_FOUND))
        } catch (e: SQLException) {
            emit(Result.Error(CanvasError.DATABASE_ERROR))
        } catch (e: Exception) {
            emit(Result.Error(CanvasError.UNKNOWN_ERROR))
        }
    }
}
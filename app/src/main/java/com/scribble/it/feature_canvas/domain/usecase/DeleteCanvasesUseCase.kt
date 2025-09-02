package com.scribble.it.feature_canvas.domain.usecase

import android.database.SQLException
import com.scribble.it.feature_canvas.domain.model.operation.DeleteRequest
import com.scribble.it.feature_canvas.domain.repository.CanvasRepository
import com.scribble.it.feature_canvas.domain.error.CanvasError
import com.scribble.it.feature_canvas.domain.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DeleteCanvasesUseCase(
    private val canvasRepository: CanvasRepository
) {
    operator fun invoke(deleteRequest: DeleteRequest): Flow<Result<Unit, CanvasError>> = flow {
        emit(Result.Loading)

        when (deleteRequest) {
            is DeleteRequest.Canvases -> {
                val canvases = deleteRequest.canvases
                if (canvases.isEmpty()) {
                    emit(Result.Error(CanvasError.INVALID_CANVAS_LIST))
                    return@flow
                }

                try {
                    canvasRepository.deleteCanvases(canvases)
                    emit(Result.Success(Unit))
                } catch (e: SQLException) {
                    emit(Result.Error(CanvasError.DATABASE_ERROR))
                } catch (e: Exception) {
                    emit(Result.Error(CanvasError.UNKNOWN_ERROR))
                }
            }

            is DeleteRequest.OldRecycledCanvases -> {
                val timeStampLimit = deleteRequest.timeStampLimit
                if (timeStampLimit < 0) {
                    emit(Result.Error(CanvasError.INVALID_TIME_STAMP_LIMIT))
                    return@flow
                }

                try {
                    canvasRepository.deleteOldRecycledCanvases(timeStampLimit)
                    emit(Result.Success(Unit))
                } catch (e: SQLException) {
                    emit(Result.Error(CanvasError.DATABASE_ERROR))
                } catch (e: Exception) {
                    emit(Result.Error(CanvasError.UNKNOWN_ERROR))
                }
            }
        }

    }
}
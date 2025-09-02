package com.scribble.it.feature_canvas.domain.usecase

import android.database.SQLException
import com.scribble.it.feature_canvas.domain.model.operation.ArchiveAction
import com.scribble.it.feature_canvas.domain.repository.CanvasRepository
import com.scribble.it.feature_canvas.domain.error.CanvasError
import com.scribble.it.feature_canvas.domain.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ArchiveCanvasesUseCase(
    private val canvasRepository: CanvasRepository
) {
    operator fun invoke(
        canvasIds: List<Int>,
        archiveAction: ArchiveAction
    ): Flow<Result<Unit, CanvasError>> = flow {
        emit(Result.Loading)

        if (canvasIds.isEmpty()) {
            emit(Result.Error(CanvasError.INVALID_CANVAS_ID_LIST))
            return@flow
        }

        try {
            when (archiveAction) {
                ArchiveAction.RECYCLE -> {
                    val now = System.currentTimeMillis()
                    canvasRepository.recycleCanvases(
                        canvasIds = canvasIds,
                        timeStamp = now
                    )
                }

                ArchiveAction.RESTORE -> {
                    canvasRepository.restoreCanvases(
                        canvasIds = canvasIds
                    )
                }
            }
            emit(Result.Success(Unit))
        } catch (e: SQLException) {
            emit(Result.Error(CanvasError.DATABASE_ERROR))
        } catch (e: Exception) {
            emit(Result.Error(CanvasError.UNKNOWN_ERROR))
        }
    }
}
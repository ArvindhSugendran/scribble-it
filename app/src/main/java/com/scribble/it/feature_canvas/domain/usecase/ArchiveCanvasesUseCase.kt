package com.scribble.it.feature_canvas.domain.usecase

import android.database.SQLException
import android.util.Log
import com.scribble.it.feature_canvas.domain.model.operation.ArchiveAction
import com.scribble.it.feature_canvas.domain.repository.CanvasRepository
import com.scribble.it.feature_canvas.domain.error.CanvasError
import com.scribble.it.feature_canvas.domain.model.usecaseResult.ArchiveUseCaseResult
import com.scribble.it.feature_canvas.domain.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ArchiveCanvasesUseCase @Inject constructor(
    private val canvasRepository: CanvasRepository,
) {
    operator fun invoke(
        canvasIds: Set<Long>,
        archiveAction: ArchiveAction
    ): Flow<Result<ArchiveUseCaseResult, CanvasError>> = flow {
        var count: Int? = null

        emit(Result.Loading)

        if (canvasIds.isEmpty()) {
            emit(Result.Error(CanvasError.INVALID_CANVAS_ID_LIST))
            return@flow
        }

        try {
            when (archiveAction) {
                ArchiveAction.RECYCLE -> {
                    val now = System.currentTimeMillis()
                    count = canvasRepository.recycleCanvases(
                        canvasIds = canvasIds,
                        timeStamp = now
                    )
                }

                ArchiveAction.RESTORE -> {
                    count = canvasRepository.restoreCanvases(
                        canvasIds = canvasIds
                    )
                }
            }

            val archiveUseCaseResult = ArchiveUseCaseResult(
                archiveRowsCount = count
            )
            emit(Result.Success(archiveUseCaseResult))
        } catch (e: SQLException) {
            emit(Result.Error(CanvasError.DATABASE_ERROR))
        } catch (e: Exception) {
            emit(Result.Error(CanvasError.UNKNOWN_ERROR))
        }
    }
}
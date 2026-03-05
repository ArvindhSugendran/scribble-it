package com.scribble.it.feature_canvas.domain.usecase

import android.database.SQLException
import com.scribble.it.feature_canvas.domain.model.operation.DeleteRequest
import com.scribble.it.feature_canvas.domain.repository.CanvasRepository
import com.scribble.it.feature_canvas.domain.error.CanvasError
import com.scribble.it.feature_canvas.domain.fileManager.FileManager
import com.scribble.it.feature_canvas.domain.model.usecaseResult.DeleteUseCaseResult
import com.scribble.it.feature_canvas.domain.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeleteCanvasesUseCase @Inject constructor(
    private val canvasRepository: CanvasRepository,
    private val fileManager: FileManager
) {
    operator fun invoke(deleteRequest: DeleteRequest): Flow<Result<DeleteUseCaseResult, CanvasError>> =
        flow {
            emit(Result.Loading)

            when (deleteRequest) {
                is DeleteRequest.Canvases -> {
                    val canvases = deleteRequest.canvases
                    if (canvases.isEmpty()) {
                        emit(Result.Error(CanvasError.INVALID_CANVAS_LIST))
                        return@flow
                    }

                    try {
                        val deletedCount = canvasRepository.deleteCanvases(canvases)
                        canvases.mapNotNull { it.thumbnailPath }.forEach { path ->
                            try {
                                fileManager.deleteFile(path)
                            } catch (_: Exception) {
                                emit(Result.Error(CanvasError.UNKNOWN_ERROR))
                            }
                        }
                        val result = DeleteUseCaseResult(deletedRowsCount = deletedCount)
                        emit(Result.Success(result))
                    } catch (e: SQLException) {
                        emit(Result.Error(CanvasError.DATABASE_ERROR))
                    } catch (e: Exception) {
                        emit(Result.Error(CanvasError.UNKNOWN_ERROR))
                    }
                }

                is DeleteRequest.CanvasesById -> {
                    val canvasIds = deleteRequest.canvasIds
                    if (canvasIds.isEmpty()) {
                        emit(Result.Error(CanvasError.INVALID_CANVAS_LIST))
                        return@flow
                    }

                    try {
                        val canvases = canvasRepository.getCanvasesByIds(canvasIds)
                        val deletedCount = canvasRepository.deleteCanvasesById(canvasIds)

                        canvases.mapNotNull { it.thumbnailPath }.forEach { path ->
                            try {
                                fileManager.deleteFile(path)
                            } catch (_: Exception) {
                                emit(Result.Error(CanvasError.UNKNOWN_ERROR))
                            }
                        }
                        val result = DeleteUseCaseResult(deletedRowsCount = deletedCount)
                        emit(Result.Success(result))
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
                        val deletedCount =
                            canvasRepository.deleteOldRecycledCanvases(timeStampLimit)
                        val result = DeleteUseCaseResult(deletedRowsCount = deletedCount)
                        emit(Result.Success(result))
                    } catch (e: SQLException) {
                        emit(Result.Error(CanvasError.DATABASE_ERROR))
                    } catch (e: Exception) {
                        emit(Result.Error(CanvasError.UNKNOWN_ERROR))
                    }
                }
            }

        }
}
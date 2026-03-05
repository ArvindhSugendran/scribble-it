package com.scribble.it.feature_canvas.domain.usecase

import android.database.SQLException
import com.scribble.it.feature_canvas.core.graphics.ThumbnailGenerator
import com.scribble.it.feature_canvas.core.graphics.error.GenerateErrorType
import com.scribble.it.feature_canvas.core.graphics.error.SaveErrorType
import com.scribble.it.feature_canvas.core.graphics.error.ThumbnailError
import com.scribble.it.feature_canvas.domain.model.canvas.CanvasDrawing
import com.scribble.it.feature_canvas.domain.repository.CanvasRepository
import com.scribble.it.feature_canvas.domain.error.CanvasError
import com.scribble.it.feature_canvas.domain.model.usecaseResult.SaveUsesCaseResult
import com.scribble.it.feature_canvas.domain.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SaveCanvasUseCase @Inject constructor(
    private val canvasRepository: CanvasRepository,
    private val thumbnailGenerator: ThumbnailGenerator
) {
    operator fun invoke(canvasDrawing: CanvasDrawing): Flow<Result<SaveUsesCaseResult, CanvasError>> = flow {
        emit(Result.Loading)

        if (canvasDrawing.title.isEmpty()) {
            emit(Result.Error(CanvasError.INVALID_TITLE))
            return@flow
        }

        try {
            val thumbnailPath = thumbnailGenerator.generateAndSave(canvasDrawing = canvasDrawing)

            val now = System.currentTimeMillis()
            val canvasWithTimeStampsAndThumbnail = canvasDrawing.copy(
                createdDate = canvasDrawing.id?.let { canvasDrawing.createdDate } ?: now,
                modifiedDate = now,
                thumbnailPath = thumbnailPath
            )

            val resultId = canvasRepository.upsertCanvas(canvasWithTimeStampsAndThumbnail)
            val result = SaveUsesCaseResult(
                id = resultId,
                title = canvasDrawing.title,
                thumbnailPath = thumbnailPath
            )
            emit(Result.Success(result))
        } catch (e: SQLException) {
            emit(Result.Error(CanvasError.DATABASE_ERROR))
        }  catch (e: ThumbnailError) {
            // Convert thumbnail error to canvas error with details
            when (e) {
                is ThumbnailError.GenerateError -> {
                    when (e.errorType) {
                        GenerateErrorType.BITMAP_CREATION_FAILED -> emit(Result.Error(CanvasError.THUMBNAIL_MEMORY_ERROR))
                        GenerateErrorType.CALCULATION_ERROR -> emit(Result.Error(CanvasError.THUMBNAIL_CALCULATION_ERROR))
                        GenerateErrorType.UNKNOWN -> emit(Result.Error(CanvasError.THUMBNAIL_GENERATION_FAILED))
                    }
                }

                is ThumbnailError.SaveError -> {
                    when (e.errorType) {
                        SaveErrorType.IO_ERROR -> emit(Result.Error(CanvasError.THUMBNAIL_IO_ERROR))
                        SaveErrorType.SECURITY_ERROR -> emit(Result.Error(CanvasError.THUMBNAIL_SECURITY_ERROR))
                        SaveErrorType.UNKNOWN -> emit(Result.Error(CanvasError.THUMBNAIL_SAVE_FAILED))
                    }
                }

                ThumbnailError.EmptyDataError -> emit(Result.Error(CanvasError.INVALID_CANVAS_LIST))
            }
        }
        catch (e: Exception) {
            emit(Result.Error(CanvasError.UNKNOWN_ERROR))
        }
    }
}
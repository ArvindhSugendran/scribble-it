package com.scribble.it.feature_canvas.domain.usecase

import androidx.paging.PagingData
import com.scribble.it.feature_canvas.domain.model.canvas.CanvasDrawing
import com.scribble.it.feature_canvas.domain.model.operation.SortOption
import com.scribble.it.feature_canvas.domain.repository.CanvasRepository
import kotlinx.coroutines.flow.Flow

class GetPagingCanvasesUseCase(
    private val canvasRepository: CanvasRepository
) {
    operator fun invoke(
        query: String,
        sortOption: String = SortOption.CREATED_DATE_DESC.name,
        isRecycled: Boolean = false
    ) : Flow<PagingData<CanvasDrawing>> {
        return canvasRepository.getPagingCanvases(
            query = query,
            sortOption = sortOption,
            isRecycled = isRecycled
        )
    }
}
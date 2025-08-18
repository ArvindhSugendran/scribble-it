package com.scribble.it.feature_canvas.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.scribble.it.core.util.PAGE_SIZE
import com.scribble.it.feature_canvas.data.local.db.dao.ScribbleDao
import com.scribble.it.feature_canvas.data.local.db.entities.CanvasEntity
import com.scribble.it.feature_canvas.data.mappers.toCanvasDrawing
import com.scribble.it.feature_canvas.data.mappers.toCanvasEntity
import com.scribble.it.feature_canvas.domain.model.canvas.CanvasDrawing
import com.scribble.it.feature_canvas.domain.repository.CanvasRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CanvasRepositoryImpl @Inject constructor(
    private val scribbleDao: ScribbleDao
): CanvasRepository {

    override fun getPagingCanvases(
        query: String,
        sortOption: String,
        isRecycled: Boolean
    ): Flow<PagingData<CanvasDrawing>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { scribbleDao.getPagingCanvases(
                query = query,
                sortOption = sortOption,
                isRecycled = isRecycled
            )
            }
        ).flow
            .map { value: PagingData<CanvasEntity> ->  
                value.map { canvasEntity: CanvasEntity ->
                    canvasEntity.toCanvasDrawing()
                }
            }
    }

    override suspend fun getCanvasById(canvasId: Int): CanvasDrawing? {
        return scribbleDao.getCanvasById(canvasId = canvasId)?.toCanvasDrawing()
    }

    override suspend fun upsetCanvas(canvasDrawing: CanvasDrawing) {
        scribbleDao.upsertCanvas(canvasEntity = canvasDrawing.toCanvasEntity())
    }

    override suspend fun deleteCanvasList(canvases: List<CanvasDrawing>) {
        scribbleDao.deleteCanvases(canvases = canvases.map { it.toCanvasEntity() })
    }

    override suspend fun deleteCanvas(canvasDrawing: CanvasDrawing) {
        scribbleDao.deleteCanvas(canvas = canvasDrawing.toCanvasEntity())
    }

    override suspend fun recycleCanvases(canvasIds: List<Int>, timeStamp: Long) {
        scribbleDao.recycleCanvases(canvasIds = canvasIds, timeStamp = timeStamp)
    }

    override suspend fun restoreCanvases(canvasIds: List<Int>) {
        scribbleDao.restoreCanvases(canvasIds = canvasIds)
    }

    override suspend fun deleteOldRecycledCanvases(timeStamp: Long) {
        scribbleDao.deleteOldRecycledCanvases(timeStamp)
    }

}
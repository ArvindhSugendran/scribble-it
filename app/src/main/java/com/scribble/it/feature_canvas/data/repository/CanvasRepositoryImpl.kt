package com.scribble.it.feature_canvas.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.scribble.it.feature_canvas.data.local.datastore.ScribbleDataStorePreferences
import com.scribble.it.feature_canvas.data.util.PAGE_SIZE
import com.scribble.it.feature_canvas.data.local.db.dao.ScribbleDao
import com.scribble.it.feature_canvas.data.local.db.model.CanvasSummaryEntity
import com.scribble.it.feature_canvas.data.mappers.toCanvasDrawing
import com.scribble.it.feature_canvas.data.mappers.toCanvasEntity
import com.scribble.it.feature_canvas.data.mappers.toCanvasSummary
import com.scribble.it.feature_canvas.domain.model.canvas.CanvasDrawing
import com.scribble.it.feature_canvas.domain.model.canvasSummary.CanvasSummary
import com.scribble.it.feature_canvas.domain.repository.CanvasRepository
import com.scribble.it.feature_canvas.presentation.common.state.CanvasViewMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CanvasRepositoryImpl @Inject constructor(
    private val scribbleDao: ScribbleDao,
    private val scribbleDataStorePreferences: ScribbleDataStorePreferences
) : CanvasRepository {

    override fun getPagingCanvases(
        query: String,
        sortOption: String,
        isRecycled: Boolean
    ): Flow<PagingData<CanvasSummary>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                scribbleDao.getPagingCanvases(
                    query = query,
                    sortOption = sortOption,
                    isRecycled = isRecycled
                )
            }
        ).flow
            .map { value: PagingData<CanvasSummaryEntity> ->
                value.map { canvasSummary: CanvasSummaryEntity ->
                    canvasSummary.toCanvasSummary()
                }
            }
    }

    override suspend fun getMaxAutoTitleIndex(): Int? = withContext(Dispatchers.IO) {
        scribbleDao.getMaxAutoTitleIndex()
    }

    override suspend fun getCanvasById(canvasId: Long): CanvasDrawing? =
        withContext(Dispatchers.IO) {
            scribbleDao.getCanvasById(canvasId = canvasId)?.toCanvasDrawing()
        }

    override suspend fun getCanvasesByIds(canvasIds: Set<Long>): List<CanvasDrawing> =
        withContext(Dispatchers.IO) {
            scribbleDao.getCanvasesByIds(ids = canvasIds).map { it.toCanvasDrawing() }
        }

    override suspend fun upsertCanvas(canvasDrawing: CanvasDrawing): Long =
        withContext(Dispatchers.IO) {
            scribbleDao.upsertCanvas(canvasEntity = canvasDrawing.toCanvasEntity())
        }

    override suspend fun deleteCanvases(canvases: List<CanvasDrawing>): Int =
        withContext(Dispatchers.IO) {
            scribbleDao.deleteCanvases(canvases = canvases.map { it.toCanvasEntity() })
        }

    override suspend fun deleteCanvasesById(canvasIds: Set<Long>): Int =
        withContext(Dispatchers.IO) {
            scribbleDao.deleteCanvasesById(canvasIds = canvasIds)
        }

    override suspend fun deleteOldRecycledCanvases(timeStampLimit: Long): Int =
        withContext(Dispatchers.IO) {
            scribbleDao.deleteOldRecycledCanvases(timeStampLimit)
        }

    override suspend fun recycleCanvases(canvasIds: Set<Long>, timeStamp: Long): Int =
        withContext(Dispatchers.IO) {
            scribbleDao.recycleCanvases(canvasIds = canvasIds, timeStamp = timeStamp)
        }

    override suspend fun restoreCanvases(canvasIds: Set<Long>): Int = withContext(Dispatchers.IO) {
        scribbleDao.restoreCanvases(canvasIds = canvasIds)
    }

    override suspend fun setCanvasViewMode(mode: CanvasViewMode) {
        scribbleDataStorePreferences.setCanvasViewMode(mode.name)
    }

    override fun getCanvasViewMode(): Flow<CanvasViewMode> {
        return scribbleDataStorePreferences
            .getCanvasViewMode()
            .map { mode ->
                CanvasViewMode.valueOf(mode)
            }
    }

}
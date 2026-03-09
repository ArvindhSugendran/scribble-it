package com.scribble.it.feature_canvas.domain.repository

import androidx.paging.PagingData
import com.scribble.it.feature_canvas.domain.model.canvas.CanvasDrawing
import com.scribble.it.feature_canvas.domain.model.canvasSummary.CanvasSummary
import com.scribble.it.feature_canvas.presentation.common.state.CanvasViewMode
import kotlinx.coroutines.flow.Flow

interface CanvasRepository {
    fun getPagingCanvases(query: String, sortOption: String, isRecycled: Boolean): Flow<PagingData<CanvasSummary>>

    suspend fun getMaxAutoTitleIndex(): Int?

    suspend fun getCanvasById(canvasId: Long): CanvasDrawing?

    suspend fun getCanvasesByIds(canvasIds: Set<Long>): List<CanvasDrawing>

    suspend fun upsertCanvas(canvasDrawing: CanvasDrawing): Long

    suspend fun deleteCanvases(canvases: List<CanvasDrawing>): Int

    suspend fun deleteCanvasesById(canvasIds: Set<Long>): Int

    suspend fun deleteOldRecycledCanvases(timeStampLimit: Long): Int

    suspend fun recycleCanvases(canvasIds: Set<Long>, timeStamp: Long): Int

    suspend fun restoreCanvases(canvasIds: Set<Long>): Int

    suspend fun setCanvasViewMode(mode: CanvasViewMode)

    fun getCanvasViewMode(): Flow<CanvasViewMode>
}
package com.scribble.it.feature_canvas.domain.repository

import androidx.paging.PagingData
import com.scribble.it.feature_canvas.domain.model.canvas.CanvasDrawing
import kotlinx.coroutines.flow.Flow

interface CanvasRepository {
    fun getPagingCanvases(query: String, sortOption: String, isRecycled: Boolean): Flow<PagingData<CanvasDrawing>>

    suspend fun getCanvasById(canvasId: Int): CanvasDrawing?

    suspend fun upsertCanvas(canvasDrawing: CanvasDrawing)

    suspend fun deleteCanvases(canvases: List<CanvasDrawing>)

    suspend fun recycleCanvases(canvasIds: List<Int>, timeStamp: Long)

    suspend fun restoreCanvases(canvasIds: List<Int>)

    suspend fun deleteOldRecycledCanvases(timeStampLimit: Long)
}
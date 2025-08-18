package com.scribble.it.feature_canvas.domain.repository

import androidx.paging.PagingData
import com.scribble.it.feature_canvas.domain.model.canvas.CanvasDrawing
import kotlinx.coroutines.flow.Flow

interface CanvasRepository {
    fun getPagingCanvases(query: String, sortOption: String, isRecycled: Boolean): Flow<PagingData<CanvasDrawing>>

    suspend fun getCanvasById(canvasId: Int): CanvasDrawing?

    suspend fun upsetCanvas(canvasDrawing: CanvasDrawing)

    suspend fun deleteCanvasList(canvases: List<CanvasDrawing>)

    suspend fun deleteCanvas(canvasDrawing: CanvasDrawing)

    suspend fun recycleCanvases(canvasIds: List<Int>, timeStamp: Long)

    suspend fun restoreCanvases(canvasIds: List<Int>)

    suspend fun deleteOldRecycledCanvases(timeStamp: Long)
}
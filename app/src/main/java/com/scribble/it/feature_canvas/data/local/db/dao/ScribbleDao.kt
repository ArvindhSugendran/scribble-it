package com.scribble.it.feature_canvas.data.local.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.scribble.it.feature_canvas.data.local.db.entities.CanvasEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScribbleDao {

    @Query("SELECT * FROM canvas_table WHERE deletedAt IS NULL")
    fun getListOfCanvas(): Flow<List<CanvasEntity>>

    @Query("SELECT * FROM canvas_table WHERE deletedAt IS NOT NULL")
    suspend fun getRecycledCanvas(): List<CanvasEntity>

    @Query("SELECT * FROM canvas_table WHERE id = :canvasId")
    suspend fun getCanvasById(canvasId: Int): CanvasEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCanvas(canvasEntity: CanvasEntity)

    @Delete
    suspend fun deleteCanvasList(canvasList: List<CanvasEntity>)

    @Delete
    suspend fun deleteCanvas(canvas: CanvasEntity)

    @Query("UPDATE canvas_table SET deletedAt = :timeStamp WHERE id = :canvasId")
    suspend fun recycleCanvas(canvasId: Int, timeStamp: Long)

    @Query("UPDATE canvas_table SET deletedAt = null WHERE id = :canvasId")
    suspend fun restoreCanvas(canvasId: Int)

    @Query("DELETE FROM canvas_table WHERE deletedAt IS NOT NULL AND deletedAt < :timestampLimit")
    suspend fun deleteOldRecycledCanvas(timestampLimit: Long)

}
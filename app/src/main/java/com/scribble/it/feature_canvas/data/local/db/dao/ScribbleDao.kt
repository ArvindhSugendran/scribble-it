package com.scribble.it.feature_canvas.data.local.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.scribble.it.feature_canvas.data.local.db.entities.CanvasEntity

@Dao
interface ScribbleDao {

    @Query("""SELECT * FROM canvas_table
        WHERE (
        :isRecycled AND deletedAt IS NOT NULL
        OR
        NOT :isRecycled AND deletedAt IS NUll
        )
        AND 
        title LIKE '%' || :query || '%'
        ORDER BY
        CASE WHEN :sortOption = 'TITLE_ASC' THEN title END ASC,
        CASE WHEN :sortOption = 'TITLE_DESC' THEN title END DESC,
        CASE WHEN :sortOption = 'CREATED_DATE_ASC' THEN title END ASC,
        CASE WHEN :sortOption = 'CREATED_DATE_DESC' THEN title END DESC,
        CASE WHEN :sortOption = 'MODIFIED_DATE_ASC' THEN title END ASC,
        CASE WHEN :sortOption = 'MODIFIED_DATE_DESC' THEN title END DESC
    """)
    fun getPagingCanvases(query: String, sortOption: String, isRecycled: Boolean): PagingSource<Int, CanvasEntity>

    @Query("SELECT * FROM canvas_table WHERE id = :canvasId")
    suspend fun getCanvasById(canvasId: Int): CanvasEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCanvas(canvasEntity: CanvasEntity)

    @Delete
    suspend fun deleteCanvases(canvases: List<CanvasEntity>)

    @Query("UPDATE canvas_table SET deletedAt = :timeStamp WHERE id IN (:canvasIds)")
    suspend fun recycleCanvases(canvasIds: List<Int>, timeStamp: Long)

    @Query("UPDATE canvas_table SET deletedAt = null WHERE id IN (:canvasIds)")
    suspend fun restoreCanvases(canvasIds: List<Int>)

    @Query("DELETE FROM canvas_table WHERE deletedAt IS NOT NULL AND deletedAt < :timestampLimit")
    suspend fun deleteOldRecycledCanvases(timestampLimit: Long)

}
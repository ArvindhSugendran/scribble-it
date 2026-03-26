package com.scribble.it.feature_canvas.data.local.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.scribble.it.feature_canvas.data.local.db.entities.CanvasEntity
import com.scribble.it.feature_canvas.data.local.db.model.CanvasSummaryDto

@Dao
interface ScribbleDao {
    @Query(
        """SELECT id, title, thumbnailPath, createdDate, modifiedDate, deletedDate 
        FROM canvas_table
        WHERE (
        (:isRecycled AND deletedDate IS NOT NULL)
        OR
        (NOT :isRecycled AND deletedDate IS NULL)
        )
        AND 
        title LIKE '%' || :query || '%'
        ORDER BY
        CASE WHEN :sortOption = 'TITLE_ASC' THEN title END ASC,
        CASE WHEN :sortOption = 'TITLE_DESC' THEN title END DESC,
        CASE WHEN :sortOption = 'CREATED_DATE_ASC' THEN createdDate END ASC,
        CASE WHEN :sortOption = 'CREATED_DATE_DESC' THEN createdDate END DESC,
        CASE WHEN :sortOption = 'MODIFIED_DATE_ASC' THEN modifiedDate END ASC,
        CASE WHEN :sortOption = 'MODIFIED_DATE_DESC' THEN modifiedDate END DESC,
        CASE WHEN :sortOption = 'DELETED_DATE_DESC' THEN deletedDate END DESC
    """
    )
    fun getPagingCanvases(
        query: String, sortOption: String, isRecycled: Boolean
    ): PagingSource<Int, CanvasSummaryDto>

    @Query(
        """
        SELECT MAX(CAST(SUBSTR(title, 10) AS INTEGER))
        FROM canvas_table
        WHERE title like 'Scribble %'
    """
    )
    suspend fun getMaxScribbleNumber(): Int?

    @Query("SELECT MAX(autoTitleIndex) FROM canvas_table")
    suspend fun getMaxAutoTitleIndex(): Int?

    @Query("SELECT * FROM canvas_table WHERE id = :canvasId")
    suspend fun getCanvasById(canvasId: Long): CanvasEntity?

    @Query("SELECT * FROM canvas_table WHERE id IN (:ids)")
    suspend fun getCanvasesByIds(ids: Set<Long>): List<CanvasEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCanvas(canvasEntity: CanvasEntity): Long

    @Delete
    suspend fun deleteCanvases(canvases: List<CanvasEntity>): Int

    @Query("DELETE FROM canvas_table where id in (:canvasIds)")
    suspend fun deleteCanvasesById(canvasIds: Set<Long>): Int

    @Query("DELETE FROM canvas_table WHERE deletedDate IS NOT NULL AND deletedDate < :timestampLimit")
    suspend fun deleteOldRecycledCanvases(timestampLimit: Long): Int

    @Query("UPDATE canvas_table SET deletedDate = :timeStamp WHERE id IN (:canvasIds)")
    suspend fun recycleCanvases(canvasIds: Set<Long>, timeStamp: Long): Int

    @Query("UPDATE canvas_table SET deletedDate = null WHERE id IN (:canvasIds)")
    suspend fun restoreCanvases(canvasIds: Set<Long>): Int
}
package com.scribble.it.feature_canvas.data.local.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.scribble.it.feature_canvas.data.local.db.model.CanvasStrokeDto

@Entity(tableName = "canvas_table")
data class CanvasEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val title: String,
    @ColumnInfo(name = "canvasStrokes")
    val canvasStrokesDto: List<CanvasStrokeDto>,
    val thumbnailPath: String? = null,
    val createdDate: Long,
    val modifiedDate: Long,
    val deletedAt: Long? = null
)

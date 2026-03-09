package com.scribble.it.feature_canvas.data.local.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.scribble.it.feature_canvas.data.local.db.model.CanvasStrokeEntity
import com.scribble.it.feature_canvas.presentation.canvasdraw.state.PageFormat

@Entity(tableName = "canvas_table")
data class CanvasEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long?,
    val autoTitleIndex: Int? = null,
    val title: String,
    @ColumnInfo(name = "canvasStrokes")
    val canvasStrokesDto: List<CanvasStrokeEntity>,
    val pageFormat: PageFormat,
    val pageColor: Int,
    val thumbnailPath: String?,
    val createdDate: Long,
    val modifiedDate: Long,
    val deletedDate: Long?
)

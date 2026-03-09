package com.scribble.it.feature_canvas.data.local.db.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.scribble.it.feature_canvas.data.local.converters.CanvasStrokeDtoListTypeConverter
import com.scribble.it.feature_canvas.data.local.converters.PageFormatTypeConverter
import com.scribble.it.feature_canvas.data.local.db.dao.ScribbleDao
import com.scribble.it.feature_canvas.data.local.db.entities.CanvasEntity

@Database(
    entities = [CanvasEntity::class],
    version = 2,
    exportSchema = true
)
@TypeConverters(
    CanvasStrokeDtoListTypeConverter::class,
    PageFormatTypeConverter::class
)
abstract class ScribbleDatabase : RoomDatabase() {
    abstract val scribbleDao: ScribbleDao

    companion object {
        const val DATABASE_NAME = "scribble_database"
    }
}
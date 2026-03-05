package com.scribble.it.feature_canvas.data.local.converters

import androidx.room.TypeConverter
import com.scribble.it.feature_canvas.presentation.canvasdraw.state.PageFormat

class PageFormatTypeConverter {
    @TypeConverter
    fun fromPageFormat(format: PageFormat): String = format.id

    @TypeConverter
    fun toPageFormat(id: String): PageFormat = PageFormat.fromId(id)
}
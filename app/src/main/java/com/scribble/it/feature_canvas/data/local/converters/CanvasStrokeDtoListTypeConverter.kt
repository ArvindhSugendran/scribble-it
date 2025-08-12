package com.scribble.it.feature_canvas.data.local.converters

import androidx.room.TypeConverter
import com.scribble.it.feature_canvas.data.local.db.model.CanvasStrokeDto
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class CanvasStrokeDtoListTypeConverter {

    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val type = Types.newParameterizedType(List::class.java, CanvasStrokeDto::class.java)
    private val adapter: JsonAdapter<List<CanvasStrokeDto>> = moshi.adapter(type)

    @TypeConverter
    fun fromCanvasStrokeDtoList(list: List<CanvasStrokeDto>?): String = adapter.toJson(list ?: emptyList())

    @TypeConverter
    fun toCanvasStrokeDtoList(json: String?): List<CanvasStrokeDto> = json?.let { adapter.fromJson(it) } ?: emptyList()
}
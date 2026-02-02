package com.scribble.it.feature_canvas.data.local.converters

import androidx.room.TypeConverter
import com.scribble.it.feature_canvas.data.local.db.model.CanvasStrokeEntity
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class CanvasStrokeDtoListTypeConverter {

    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val type = Types.newParameterizedType(List::class.java, CanvasStrokeEntity::class.java)
    private val adapter: JsonAdapter<List<CanvasStrokeEntity>> = moshi.adapter(type)

    @TypeConverter
    fun fromCanvasStrokeDtoList(list: List<CanvasStrokeEntity>?): String = adapter.toJson(list ?: emptyList())

    @TypeConverter
    fun toCanvasStrokeDtoList(json: String?): List<CanvasStrokeEntity> = json?.let { adapter.fromJson(it) } ?: emptyList()
}
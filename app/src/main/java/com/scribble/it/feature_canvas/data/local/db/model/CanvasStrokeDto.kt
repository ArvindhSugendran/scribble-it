package com.scribble.it.feature_canvas.data.local.db.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CanvasStrokeDto(
    @Json(name = "x") val x: Float,
    @Json(name = "y") val y: Float,
    @Json(name = "penType") val penType: String,
    @Json(name = "brushSizePx") val brushSizePx: Float,
    @Json(name = "brushSizeNormalized") val brushSizeNormalized: Float,
    @Json(name = "colorArgb") val colorArgb: Int
)
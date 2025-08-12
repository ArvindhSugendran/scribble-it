package com.scribble.it.feature_canvas.domain.model.stroke

import androidx.compose.ui.geometry.Offset

data class Dot(
    val offset: Offset,
    val progress: Float = 0f,
    val radius: Float,
    val color: Int
)

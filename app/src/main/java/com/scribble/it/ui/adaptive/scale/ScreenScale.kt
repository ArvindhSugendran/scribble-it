package com.scribble.it.ui.adaptive.scale

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.min

class ScreenScale(
    val width: Dp,
    val height: Dp
) {
    fun w(fraction: Float): Dp = width * fraction
    fun h(fraction: Float): Dp = height * fraction
    fun min(fraction: Float): Dp = min(width, height) * fraction
}
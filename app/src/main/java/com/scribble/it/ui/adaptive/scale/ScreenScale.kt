package com.scribble.it.ui.adaptive.scale

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min

object CanvasConstants {
    // Virtual page size (does NOT depend on screen)
    const val PAGE_WIDTH = 1000f
    // A4 ratio ≈ 1 : 1.414
    const val PAGE_HEIGHT = PAGE_WIDTH * 1.414f
}

class ScreenScale(
    val width: Dp,
    val height: Dp
) {
    companion object {
        val VERY_NARROW_WIDTH = 300.dp
        val VERY_NARROW_HEIGHT = 300.dp
    }

    fun w(fraction: Float): Dp = width * fraction
    fun h(fraction: Float): Dp = height * fraction
    fun min(fraction: Float): Dp = min(width, height) * fraction

    val isVeryNarrowWidth: Boolean
        get() = width < VERY_NARROW_WIDTH

    val isVeryNarrowHeight: Boolean
        get() = height < VERY_NARROW_HEIGHT

    fun constrainedSize(aspectRatio: Float): Pair<Dp, Dp> {
        return if(width < height) {
            val w = width
            val h = width / aspectRatio
             w to h
        } else {
            val w = height * aspectRatio
            val h = height
            w to h
        }
    }
}
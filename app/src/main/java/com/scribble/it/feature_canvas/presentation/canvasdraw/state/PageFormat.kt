package com.scribble.it.feature_canvas.presentation.canvasdraw.state

sealed class PageFormat(
    val id: String,
    val aspectRatio: Float
) {
    data object A4 : PageFormat(
        id = "A4",
        aspectRatio = 210f / 297f
    )

    data object Square : PageFormat(
        id = "SQUARE",
        aspectRatio = 1f
    )

    data object Landscape16x9 : PageFormat(
        id = "LANDSCAPE_16_9",
        aspectRatio = 16f / 9f
    )

    data object Portrait16x9 : PageFormat(
        id = "PORTRAIT_16_9",
        aspectRatio = 9f / 16f
    )

    companion object {
        fun fromId(id: String): PageFormat =
            when (id) {
                A4.id -> A4
                Square.id -> Square
                Landscape16x9.id -> Landscape16x9
                Portrait16x9.id -> Portrait16x9
                else -> A4
            }
    }
}
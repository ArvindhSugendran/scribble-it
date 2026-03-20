package com.scribble.it.feature_canvas.presentation.canvaslist.state

sealed interface PaneMode {
    data object SinglePane : PaneMode
    sealed interface TwoPane : PaneMode {
        data object FullList : TwoPane
        data object Split : TwoPane
        data object FullPreview : TwoPane
    }
}
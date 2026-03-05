package com.scribble.it.feature_canvas.presentation.canvaslist.state

sealed interface PaneState {
    data object SinglePane : PaneState
    sealed interface TwoPane : PaneState {
        data object FullList : TwoPane
        data object Split : TwoPane
        data object FullPreview : TwoPane
    }
}
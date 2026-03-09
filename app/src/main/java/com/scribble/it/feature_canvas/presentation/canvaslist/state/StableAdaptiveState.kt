package com.scribble.it.feature_canvas.presentation.canvaslist.state

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

class StableAdaptiveState {
    private var frozenWidth by mutableStateOf<Dp?>(null)
    private var halfWidth by mutableStateOf<Dp?>(null)

    fun halfWidth(width: Dp) {
        if(halfWidth == null) halfWidth = width
    }

    fun freeze(width: Dp) {
        if (frozenWidth == null) frozenWidth = width
        Log.d("METRICS_WIDTH", "Width at freezeTime: $frozenWidth")
    }

    fun commit(localReveal: Float, width: Dp?) {
        frozenWidth = if((localReveal == 0.5f && width == null) || width == 0.dp) {
            halfWidth
        } else {
            width
        }
        Log.d("METRICS_WIDTH", "Width at commitTime: $frozenWidth")
    }

    fun current(): Dp? = frozenWidth
}
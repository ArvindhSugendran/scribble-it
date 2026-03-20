package com.scribble.it.feature_canvas.presentation.canvasdraw.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Stop
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.scribble.it.feature_canvas.presentation.canvasdraw.state.ReplayState

@Composable
fun CanvasReplayControls(
    modifier: Modifier = Modifier,
    replayState: ReplayState,
    onReplay: () -> Unit,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    onStop: () -> Unit
) {
    val itemPadding = 24.dp

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(itemPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {

        ReplayControlButton(
            icon = Icons.Default.Refresh,
            active = false,
            onClick = onReplay
        )

        ReplayControlButton(
            icon = if (replayState == ReplayState.PLAYING)
                Icons.Default.Pause
            else
                Icons.Default.PlayArrow,

            active = replayState == ReplayState.PLAYING,

            onClick = {
                if (replayState == ReplayState.PLAYING) {
                    onPause()
                } else {
                    onPlay()
                }
            }
        )

        ReplayControlButton(
            icon = Icons.Default.Stop,
            active = replayState == ReplayState.IDLE,
            onClick = onStop
        )
    }
}
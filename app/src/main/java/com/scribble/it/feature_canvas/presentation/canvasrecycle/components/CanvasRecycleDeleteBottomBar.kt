package com.scribble.it.feature_canvas.presentation.canvasrecycle.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CanvasRecycleDeleteBottomBar(
    modifier: Modifier = Modifier,
    onDelete: () -> Unit,
    onRestore: () -> Unit
) {
    Row (
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ){
        TextButton(
            onClick = onRestore
        ) {
            Text(
                text = "RESTORE",
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Box(
            modifier = Modifier
                .width(1.dp)
                .height(16.dp)
                .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.7f))
        )

        TextButton(
            onClick = onDelete
        ) {
            Text(
                text = "DELETE",
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

    }
}

@Preview
@Composable
fun PreviewCanvasRecycleDeleteBottomBar() {
    Box(
        Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface)
    ) {
        CanvasRecycleDeleteBottomBar(
            modifier = Modifier
                .navigationBarsPadding()
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            onDelete = {},
            onRestore = {},
        )
    }

}
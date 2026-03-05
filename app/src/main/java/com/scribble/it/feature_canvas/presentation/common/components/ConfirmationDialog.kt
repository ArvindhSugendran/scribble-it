package com.scribble.it.feature_canvas.presentation.common.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.scribble.it.feature_canvas.presentation.common.dialog.state.ConfirmationDialogState

@Composable
fun ConfirmationDialog(
    state: ConfirmationDialogState,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (!state.isVisible) return

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(state.title) },
        text = { Text(state.message) },
        confirmButton = {
            TextButton (onClick = onConfirm) {
                Text(state.confirmText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(state.dismissText)
            }
        }
    )
}
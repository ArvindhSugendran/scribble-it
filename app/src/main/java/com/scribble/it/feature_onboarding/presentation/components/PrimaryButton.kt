package com.scribble.it.feature_onboarding.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.scribble.it.ui.theme.Graphite90

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    text: String,
    fontSize: TextUnit,
    onClick: () -> Unit,
    contentPaddingValues: PaddingValues = PaddingValues(16.dp),
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        colors = ButtonDefaults.buttonColors(
            containerColor = Graphite90.copy(alpha = 0.3f),
            contentColor = Color.White
        ),
        contentPadding = contentPaddingValues
    ) {
        Text(
            text = text,
            fontSize = fontSize
        )
    }
}
package com.scribble.it.feature_onboarding.presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowRight
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import com.scribble.it.ui.theme.Graphite90

@Composable
fun NextButton(
    modifier: Modifier = Modifier,
    fontSize: TextUnit,
    horizontalPadding: Dp,
    onClick: () -> Unit,
) {
    FilledTonalButton(
        onClick = onClick,
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = Graphite90.copy(alpha = 0.3f),
            contentColor = Color.White
        )
    ) {
        Text(
            text = "Next",
            fontSize = fontSize
        )
        Spacer(modifier = Modifier.width(horizontalPadding / 4))
        Icon(
            imageVector = Icons.AutoMirrored.Outlined.ArrowRight,
            contentDescription = "Next"
        )
    }
}
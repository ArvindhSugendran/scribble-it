package com.scribble.it.feature_onboarding.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.scribble.it.feature_onboarding.domain.model.OnboardingPage

@Composable
fun OnboardingPage(
    modifier: Modifier = Modifier,
    pageData: OnboardingPage,
    screenHeight: Dp,
    screenWidth: Dp
) {
    // Calculate responsive dimensions
    val illustrationSize = screenWidth * 0.6f // 60% of screen width
    val iconSize = illustrationSize * 0.6f // 60% of illustration size
    val verticalSpacing = screenHeight * 0.05f // 5% of screen height
    val horizontalPadding = screenWidth * 0.08f // 8% of screen width

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Spacer(modifier = Modifier.weight(0.2f)) // 20% top space

        // Responsive illustration container
        Box(
            modifier = Modifier
                .size(illustrationSize)
                .background(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    shape = MaterialTheme.shapes.large,
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = pageData.imageVector,
                contentDescription = "Illustration",
                modifier = Modifier.size(iconSize),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(verticalSpacing))

        // Responsive text content
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(0.4f) // 40% for text content
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding)
        ) {
            Text(
                modifier = Modifier.padding(bottom = verticalSpacing * 0.5f),
                text = pageData.title,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            Text(
                text = pageData.description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.weight(0.3f)) // 30% bottom space
    }
}
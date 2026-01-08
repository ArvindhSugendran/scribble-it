package com.scribble.it.feature_onboarding.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun OnboardingBottomSection(
    modifier: Modifier = Modifier,
    currentPage: Int,
    totalPages: Int,
    onNextClicked: () -> Unit,
    onSkipClicked: () -> Unit,
    onGetStartedClicked: () -> Unit,
    screenWidth: Dp,
    screenHeight: Dp
) {
    // Responsive dimensions
    val sectionHeight = screenHeight * 0.2f // 20% of screen height
    val horizontalPadding = screenWidth * 0.08f // 8% of screen width
    val verticalPadding = screenHeight * 0.02f // 2% of screen height
    val buttonHeight = screenHeight * 0.06f // 6% of screen height

    Column(
        modifier = modifier
            .height(sectionHeight)
            .padding(
                horizontal = horizontalPadding,
                vertical = verticalPadding
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        // Page Indicators
        PageIndicators(
            modifier = Modifier.padding(bottom = verticalPadding),
            currentPage = currentPage,
            totalPages = totalPages
        )

        // Action Buttons
        if (currentPage == totalPages - 1) {
            // Last page - Show Get Started button
            PrimaryButton(
                modifier = Modifier.fillMaxWidth().height(buttonHeight),
                text = "Get Started",
                onClick = onGetStartedClicked,
            )
        } else {
            // Other pages - Show Skip and Next
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onSkipClicked) {
                    Text(
                        text = "Skip",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                NextButton(
                    modifier = Modifier.height(buttonHeight),
                    onClick = { onNextClicked() },
                )
            }
        }
    }
}
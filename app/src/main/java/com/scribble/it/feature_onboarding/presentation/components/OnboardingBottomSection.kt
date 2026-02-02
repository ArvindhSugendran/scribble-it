package com.scribble.it.feature_onboarding.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.scribble.it.feature_onboarding.ui.OnboardingBottomMetrics

@Composable
fun OnboardingBottom(
    modifier: Modifier = Modifier,
    currentPage: Int,
    totalPages: Int,
    onNextClicked: () -> Unit,
    onSkipClicked: () -> Unit,
    onGetStartedClicked: () -> Unit,
    metrics: OnboardingBottomMetrics
) {

    val rootModifier = modifier.padding(
        horizontal = metrics.horizontalPadding,
        vertical = metrics.verticalSpacing
    )

    OnboardingBottomContent(
        modifier = rootModifier,
        currentPage = currentPage,
        totalPages = totalPages,
        dotSize = metrics.dotSize,
        buttonTextFontSize = metrics.buttonTextFontSize,
        onNextClicked = onNextClicked,
        onSkipClicked = onSkipClicked,
        onGetStartedClicked = onGetStartedClicked,
        horizontalPadding = metrics.horizontalPadding,
        verticalSpacing = metrics.verticalSpacing
    )
}

@Composable
fun OnboardingBottomContent(
    modifier: Modifier = Modifier,
    currentPage: Int,
    totalPages: Int,
    dotSize: Dp,
    buttonTextFontSize: TextUnit,
    onNextClicked: () -> Unit,
    onSkipClicked: () -> Unit,
    onGetStartedClicked: () -> Unit,
    horizontalPadding: Dp,
    verticalSpacing: Dp
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        // Page Indicators
        PageIndicators(
            modifier = Modifier.padding(bottom = verticalSpacing),
            currentPage = currentPage,
            totalPages = totalPages,
            dotSize = dotSize
        )

        // Action Buttons
        if (currentPage == totalPages - 1) {
            // Last page - Show Get Started button
            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Get Started",
                fontSize = buttonTextFontSize,
                onClick = onGetStartedClicked,
                contentPaddingValues = PaddingValues(
                    horizontal = horizontalPadding / 2, vertical = verticalSpacing / 2
                )
            )
        } else {
            // Other pages - Show Skip and Next
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = onSkipClicked
                ) {
                    Text(
                        text = "Skip",
                        fontSize = buttonTextFontSize,
                        color = Color.White
                    )
                }

                NextButton(
                    modifier = Modifier,
                    fontSize = buttonTextFontSize,
                    horizontalPadding = horizontalPadding,
                    onClick = { onNextClicked() }
                )
            }
        }
    }
}
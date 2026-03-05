package com.scribble.it.feature_onboarding.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.scribble.it.feature_onboarding.domain.model.OnboardingPage
import com.scribble.it.feature_onboarding.ui.OnboardingPageMetrics

@Composable
fun OnboardingPage(
    modifier: Modifier = Modifier,
    pageData: OnboardingPage,
    metrics: OnboardingPageMetrics,
) {
    OnboardingPageContent(
        modifier = modifier,
        pageData = pageData,
        illustrationSize = metrics.illustrationSize,
        horizontalPadding = metrics.horizontalPadding,
        verticalSpacing = metrics.verticalSpacing,
        titleFontSize = metrics.titleFontSize,
        descriptionFontSize = metrics.descriptionFontSize
    )
}

@Composable
fun OnboardingPageContent(
    modifier: Modifier = Modifier,
    pageData: OnboardingPage,
    illustrationSize: Dp,
    horizontalPadding: Dp,
    verticalSpacing: Dp,
    titleFontSize: TextUnit = 28.sp,
    descriptionFontSize: TextUnit = 16.sp) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(verticalSpacing))

        Box(
            modifier = Modifier
                .size(illustrationSize)
                .aspectRatio(1f)
                .background(
                    color = Color.Transparent,
                    shape = MaterialTheme.shapes.large,
                )
                .innerShadow(
                    shape = MaterialTheme.shapes.large,
                    shadow = Shadow(
                        radius = 10.dp,
                        spread = 4.dp,
                        color = Color(0x40000000),
                        offset = DpOffset(x = 6.dp, 7.dp)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = pageData.imageVector,
                contentDescription = "Illustration",
                modifier = Modifier.fillMaxSize(0.6f),
                tint = Color.White
            )
        }

        Spacer(modifier = Modifier.height(verticalSpacing))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(horizontal = horizontalPadding)
        ) {
            Text(
                text = pageData.title,
                fontSize = titleFontSize,
                fontWeight = FontWeight.Bold,
                lineHeight = titleFontSize * 1.2f,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(verticalSpacing / 2))

            Text(
                text = pageData.description,
                fontSize = descriptionFontSize,
                lineHeight = descriptionFontSize * 1.5f,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}
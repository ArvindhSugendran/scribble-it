package com.scribble.it.feature_onboarding.ui

import androidx.compose.ui.unit.sp
import com.scribble.it.ui.adaptive.scale.HeightClass
import com.scribble.it.ui.adaptive.layoutConfig.LayoutConfiguration
import com.scribble.it.ui.adaptive.scale.ScreenScale
import com.scribble.it.ui.adaptive.scale.WidthClass

fun retrieveOnboardingBottomMetrics(
    scale: ScreenScale,
    layout: LayoutConfiguration
): OnboardingBottomMetrics {
    return when (layout.width) {
        WidthClass.COMPACT -> compactMetrics(scale, layout.height)
        WidthClass.MEDIUM -> mediumMetrics(scale, layout.height)
        WidthClass.EXPANDED -> expandedMetrics(scale, layout.height)
        WidthClass.LARGE,
        WidthClass.EXTRA_LARGE -> TODO()
    }
}

private fun compactMetrics(
    s: ScreenScale,
    height: HeightClass
): OnboardingBottomMetrics {

    return when (height) {
        HeightClass.COMPACT -> {
            OnboardingBottomMetrics(
                horizontalPadding = s.w(0.04f),
                verticalSpacing = s.h(0.02f),
                dotSize = s.min(0.04f),
                buttonTextFontSize = 12.sp
            )
        }

        HeightClass.MEDIUM -> {
            OnboardingBottomMetrics(
                horizontalPadding = s.w(0.06f),
                verticalSpacing = s.h(0.025f),
                dotSize = s.min(0.04f),
                buttonTextFontSize = 16.sp
            )
        }

        HeightClass.EXPANDED -> {
            OnboardingBottomMetrics(
                horizontalPadding = s.w(0.08f),
                verticalSpacing = s.h(0.03f),
                dotSize = s.min(0.04f),
                buttonTextFontSize = 16.sp
            )
        }
    }
}

private fun mediumMetrics(
    s: ScreenScale,
    height: HeightClass
): OnboardingBottomMetrics {

    return when (height) {
        HeightClass.COMPACT -> {
            OnboardingBottomMetrics(
                horizontalPadding = s.w(0.04f),
                verticalSpacing = s.h(0.025f),
                dotSize = s.min(0.025f),
                buttonTextFontSize = 15.sp
            )
        }

        HeightClass.MEDIUM -> {
            OnboardingBottomMetrics(
                horizontalPadding = s.w(0.06f),
                verticalSpacing = s.h(0.025f),
                dotSize = s.min(0.025f),
                buttonTextFontSize = 16.sp
            )
        }

        HeightClass.EXPANDED -> {
            OnboardingBottomMetrics(
                horizontalPadding = s.w(0.08f),
                verticalSpacing = s.h(0.03f),
                dotSize = s.min(0.03f),
                buttonTextFontSize = 16.sp
            )
        }
    }
}

private fun expandedMetrics(
    s: ScreenScale,
    height: HeightClass
): OnboardingBottomMetrics {

    return when (height) {
        HeightClass.COMPACT -> {
            OnboardingBottomMetrics(
                horizontalPadding = s.w(0.04f),
                verticalSpacing = s.h(0.025f),
                dotSize = s.min(0.04f),
                buttonTextFontSize = 14.sp
            )
        }

        HeightClass.MEDIUM -> {
            OnboardingBottomMetrics(
                horizontalPadding = s.w(0.06f),
                verticalSpacing = s.h(0.025f),
                dotSize = s.min(0.025f),
                buttonTextFontSize = 16.sp
            )
        }

        HeightClass.EXPANDED -> {
            OnboardingBottomMetrics(
                horizontalPadding = s.w(0.08f),
                verticalSpacing = s.h(0.03f),
                dotSize = s.min(0.03f),
                buttonTextFontSize = 16.sp
            )
        }
    }
}
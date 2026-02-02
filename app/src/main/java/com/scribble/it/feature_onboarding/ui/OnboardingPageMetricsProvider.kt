package com.scribble.it.feature_onboarding.ui

import androidx.compose.ui.unit.sp
import com.scribble.it.ui.adaptive.scale.HeightClass
import com.scribble.it.ui.adaptive.layoutConfig.LayoutConfiguration
import com.scribble.it.ui.adaptive.scale.ScreenScale
import com.scribble.it.ui.adaptive.scale.WidthClass

fun retrieveOnboardingPageMetrics(
    scale: ScreenScale,
    layout: LayoutConfiguration
): OnboardingPageMetrics {

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
): OnboardingPageMetrics {

    return when (height) {
        HeightClass.COMPACT -> {
            OnboardingPageMetrics(
                illustrationSize = s.min(0.32f),
                horizontalPadding = s.w(0.05f),
                verticalSpacing = s.h(0.025f),
                titleFontSize = 16.sp,
                descriptionFontSize = 12.sp
            )
        }

        HeightClass.MEDIUM -> {
            OnboardingPageMetrics(
                illustrationSize = s.min(0.5f),
                horizontalPadding = s.w(0.06f),
                verticalSpacing = s.h(0.025f),
                titleFontSize = 18.sp,
                descriptionFontSize = 14.sp
            )
        }

        HeightClass.EXPANDED -> {
            OnboardingPageMetrics(
                illustrationSize = s.min(0.6f),
                horizontalPadding = s.w(0.08f),
                verticalSpacing = s.h(0.02f),
                titleFontSize = 28.sp,
                descriptionFontSize = 16.sp
            )
        }
    }
}

private fun mediumMetrics(
    s: ScreenScale,
    height: HeightClass
): OnboardingPageMetrics {

    return when (height) {
        HeightClass.COMPACT -> {
            OnboardingPageMetrics(
                illustrationSize = s.min(0.32f),
                horizontalPadding = s.w(0.04f),
                verticalSpacing = s.h(0.03f),
                titleFontSize = 20.sp,
                descriptionFontSize = 16.sp
            )
        }

        HeightClass.MEDIUM -> {
            OnboardingPageMetrics(
                illustrationSize = s.min(0.4f),
                horizontalPadding = s.w(0.06f),
                verticalSpacing = s.h(0.02f),
                titleFontSize = 28.sp,
                descriptionFontSize = 16.sp
            )
        }

        HeightClass.EXPANDED -> {
            OnboardingPageMetrics(
                illustrationSize = s.min(0.6f),
                horizontalPadding = s.w(0.06f),
                verticalSpacing = s.h(0.04f),
                titleFontSize = 28.sp,
                descriptionFontSize = 16.sp
            )
        }
    }
}

private fun expandedMetrics(
    s: ScreenScale,
    height: HeightClass
): OnboardingPageMetrics {

    return when (height) {
        HeightClass.COMPACT -> {
            OnboardingPageMetrics(
                illustrationSize = s.min(0.4f),
                horizontalPadding = s.w(0.04f),
                verticalSpacing = s.h(0.03f),
                titleFontSize = 20.sp,
                descriptionFontSize = 16.sp
            )
        }

        HeightClass.MEDIUM -> {
            OnboardingPageMetrics(
                illustrationSize = s.min(0.5f),
                horizontalPadding = s.w(0.06f),
                verticalSpacing = s.h(0.02f),
                titleFontSize = 28.sp,
                descriptionFontSize = 16.sp
            )
        }

        HeightClass.EXPANDED -> {
            OnboardingPageMetrics(
                illustrationSize = s.min(0.6f),
                horizontalPadding = s.w(0.06f),
                verticalSpacing = s.h(0.04f),
                titleFontSize = 28.sp,
                descriptionFontSize = 16.sp
            )
        }
    }
}

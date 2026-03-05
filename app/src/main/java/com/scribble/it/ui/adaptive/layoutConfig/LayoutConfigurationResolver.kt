package com.scribble.it.ui.adaptive.layoutConfig

import android.content.res.Configuration
import android.util.Log
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import com.scribble.it.ui.adaptive.scale.HeightClass
import com.scribble.it.ui.adaptive.scale.WidthClass

fun resolveWidthClass(windowSizeClass: WindowSizeClass): WidthClass {
    return when {
        windowSizeClass.isWidthAtLeastBreakpoint(1600) -> WidthClass.EXTRA_LARGE
        windowSizeClass.isWidthAtLeastBreakpoint(1200) -> WidthClass.LARGE
        windowSizeClass.isWidthAtLeastBreakpoint(840) -> WidthClass.EXPANDED
        windowSizeClass.isWidthAtLeastBreakpoint(600) -> WidthClass.MEDIUM
        else -> WidthClass.COMPACT
    }
}

fun resolveHeightClass(windowSizeClass: WindowSizeClass): HeightClass {
    return when {
        windowSizeClass.isHeightAtLeastBreakpoint(900) -> HeightClass.EXPANDED
        windowSizeClass.isHeightAtLeastBreakpoint(480) -> HeightClass.MEDIUM
        else -> HeightClass.COMPACT
    }
}

fun resolveWidthClass(widthDp: Dp): WidthClass {
    return when {
        widthDp >= 1600.dp -> WidthClass.EXTRA_LARGE
        widthDp >= 1200.dp -> WidthClass.LARGE
        widthDp >= 840.dp -> WidthClass.EXPANDED
        widthDp >= 600.dp -> WidthClass.MEDIUM
        else -> WidthClass.COMPACT
    }
}

fun resolveHeightClass(heightDp: Dp): HeightClass {
    return when {
        heightDp >= 900.dp -> HeightClass.EXPANDED
        heightDp >= 480.dp -> HeightClass.MEDIUM
        else -> HeightClass.COMPACT
    }
}

fun isTwoPaneAllowed(
    widthDp: Dp,
    heightDp: Dp
): Boolean {
    return widthDp >= 600.dp &&
            heightDp >= 580.dp
}

fun getLayoutConfiguration(
    windowSizeClass: WindowSizeClass,
    configuration: Configuration
): LayoutConfiguration {

    val layoutConfig = LayoutConfiguration(
        width = resolveWidthClass(windowSizeClass),
        height = resolveHeightClass(windowSizeClass)
    )

    val description = logLayoutConfiguration(layoutConfig)

    Log.d(
        "LayoutConfig",
        """
            WidthClass  = ${layoutConfig.width}
            HeightClass = ${layoutConfig.height}       
            WidthDp     = ${configuration.screenWidthDp} dp
            HeightDp    = ${configuration.screenHeightDp} dp
            Insight     = $description
            """.trimIndent()
    )

    return layoutConfig
}

fun getPaneLayoutConfiguration(
    widthDp: Dp,
    heightDp: Dp
): LayoutConfiguration {

    val layoutConfig = LayoutConfiguration(
        width = resolveWidthClass(widthDp),
        height = resolveHeightClass(heightDp)
    )

    val description = logLayoutConfiguration(layoutConfig)

    Log.d(
        "PaneConfig",
        """
            WidthClass  = ${layoutConfig.width}
            HeightClass = ${layoutConfig.height}       
            WidthDp     = $widthDp
            HeightDp    = $heightDp
            Insight     = $description
            """.trimIndent()
    )

    return layoutConfig
}

fun logLayoutConfiguration(config: LayoutConfiguration): String {
    return when {
        config.width == WidthClass.COMPACT && config.height == HeightClass.COMPACT ->
            "Very constrained space (phone landscape / freeform or resizable window / split screen)"

        config.width == WidthClass.COMPACT &&
                (config.height == HeightClass.MEDIUM || config.height == HeightClass.EXPANDED) ->
            "Narrow but tall space (phone portrait / folded foldable)"

        config.width == WidthClass.MEDIUM && config.height == HeightClass.COMPACT ->
            "Medium width but short height (tablet / foldable landscape, two-pane risky)"

        config.width == WidthClass.MEDIUM &&
                (config.height == HeightClass.MEDIUM || config.height == HeightClass.EXPANDED) ->
            "Balanced medium space (tablet or unfolded foldable, two-pane works)"

        config.width == WidthClass.EXPANDED && config.height == HeightClass.COMPACT ->
            "Very wide but short (unfolded foldable landscape, height constrained)"

        config.width == WidthClass.EXPANDED &&
                (config.height == HeightClass.MEDIUM || config.height == HeightClass.EXPANDED) ->
            "Large canvas (tablet landscape / desktop-like)"

        config.width == WidthClass.LARGE || config.width == WidthClass.EXTRA_LARGE ->
            "Desktop-class width (multi-column layouts)"

        else ->
            "Unusual but valid window configuration"
    }
}
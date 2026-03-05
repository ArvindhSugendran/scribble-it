package com.scribble.it.feature_canvas.presentation.canvaslist.ui.adaptive.pane

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.scribble.it.ui.adaptive.layoutConfig.LayoutConfiguration
import com.scribble.it.ui.adaptive.layoutConfig.getPaneLayoutConfiguration
import com.scribble.it.ui.adaptive.scale.ScreenScale

@Composable
fun <T> AdaptiveMetricsPane(
    modifier: Modifier = Modifier,
    buildMetrics: @Composable (scale: ScreenScale, layout: LayoutConfiguration) -> T,
    provide: @Composable (T, @Composable () -> Unit) -> Unit,
    content: @Composable () -> Unit
) {
    BoxWithConstraints(modifier) {
        val scale = remember(maxWidth, maxHeight) {
            ScreenScale(maxWidth, maxHeight)
        }
        val paneLayoutConfig = remember(maxWidth, maxHeight) {
            getPaneLayoutConfiguration(maxWidth, maxHeight)
        }
        val metrics = buildMetrics(scale, paneLayoutConfig)
        provide(metrics) {
            content()
        }
    }
}
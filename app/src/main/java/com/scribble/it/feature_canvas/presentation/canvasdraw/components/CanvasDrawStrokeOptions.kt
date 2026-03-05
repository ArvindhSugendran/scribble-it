package com.scribble.it.feature_canvas.presentation.canvasdraw.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.scribble.it.feature_canvas.presentation.canvasdraw.state.BrushSize

@Composable
fun CanvasDrawStokeOptions(
    modifier: Modifier = Modifier,
    selectedBrush: BrushSize,
    selectedPageColor: Color,
    selectedStrokeColor: Color,
    strokeColorPalette: List<Color>,
    onSelectBrush: (BrushSize) -> Unit,
    onSelectPageColor: (Color) -> Unit,
    onSelectStrokeColor: (Color) -> Unit
) {
    val itemSize = 36.dp
    val itemPadding = 25.dp
    val itemCount = BrushSize.entries.size
    val horizontalPadding = 16.dp * 2

    val rowWidth = (itemSize * itemCount) +
            (itemPadding * (itemCount - 1)) +
            horizontalPadding

    val verticalScrollState = rememberScrollState()
    val horizontalScrollState = rememberScrollState()

    Log.d("SELECTED_COLOR", selectedStrokeColor.toString())

    Column(
        modifier = modifier
            .verticalScroll(verticalScrollState)
            .horizontalScroll(horizontalScrollState)
            .requiredWidth(rowWidth)
            .clip(RoundedCornerShape(24.dp))
            .background(
                Color.White.copy(alpha = 0.15f)
            )
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.3f),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        color = Color.White,
                        shape = CircleShape
                    )
                    .clickable {
                        onSelectPageColor(Color.White)
                    }
            ) {
                if (selectedPageColor == Color.White) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Selected",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxSize(0.5f)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        color = Color.Black,
                        shape = CircleShape
                    )
                    .clickable {
                        onSelectPageColor(Color.Black)
                    }
            ) {
                if (selectedPageColor == Color.Black) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Selected",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxSize(0.5f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(15.dp))

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = Color.White.copy(alpha = 0.3f)
        )

        Spacer(modifier = Modifier.height(15.dp))

        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(itemPadding)
        ) {
            BrushSize.entries.forEach { brush ->
                val dotSize = (mmToDp(brush.mm) * 2f)
                Log.d("DOT_SIZE", dotSize.toString())
                Box(
                    modifier = Modifier
                        .size(itemSize)
                        .clip(CircleShape)
                        .background(
                            color = if (selectedBrush == brush)
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
                            else
                                Color.Transparent
                        )
                        .clickable { onSelectBrush(brush) },
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(dotSize)
                            .background(
                                selectedStrokeColor,
                                CircleShape
                            )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(15.dp))

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = Color.White.copy(alpha = 0.3f)
        )

        Spacer(modifier = Modifier.height(15.dp))

        //lazy row with different colors
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(itemPadding - 1.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(strokeColorPalette) { color ->
                Box(
                    modifier = Modifier
                        .size(itemSize)
                        .clip(CircleShape)
                        .background(color)
                        .clickable {
                            onSelectStrokeColor(color)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (selectedStrokeColor == color) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Selected color",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .fillMaxSize(0.5f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun mmToDp(mm: Float): Dp {
    val metrics = LocalResources.current.displayMetrics
    val density = LocalDensity.current

    val px = mm * (metrics.densityDpi / 25.4f)
    return with(density) { px.toDp() }
}

@Preview
@Composable
fun CanvasDrawStrokeOptionsPreview() {
    CanvasDrawStokeOptions(
        modifier = Modifier
            .fillMaxWidth(),
        selectedBrush = BrushSize.VERY_THIN,
        selectedPageColor = Color.White,
        selectedStrokeColor = Color.Black,
        strokeColorPalette = listOf(
            Color(0xFF000000),
            Color(0xFFFFFFFF),
            Color(0xFF444444),
            Color(0xFF888888),
            Color(0xFFCCCCCC)
        ),
        onSelectBrush = {},
        onSelectPageColor = {},
        onSelectStrokeColor = {}
    )
}
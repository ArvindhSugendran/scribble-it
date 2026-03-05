package com.scribble.it.feature_canvas.presentation.canvaslist.components.canvasPane

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

@Composable
fun CanvasSearchBar(
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester,
    cornerRadius: Dp = 24.dp,
    query: TextFieldValue,
    isBlurred: Boolean,
    enabled: Boolean,
    onQueryChange: (TextFieldValue) -> Unit,
    onFocusChange: (Boolean) -> Unit,
    onSearch: () -> Unit,
    onCloseSearchBar: () -> Unit
) {
    Surface(
        modifier = modifier
            .then(
                if (enabled) {
                    Modifier.dropShadow(
                        shape = RoundedCornerShape(cornerRadius),
                        shadow = Shadow(
                            radius = 5.dp,
                            spread = 0.dp,
                            color = Color(0x40000000),
                            offset = DpOffset(x = 2.dp, y = 3.dp)
                        )
                    )
                } else {
                    Modifier
                }
            ),
        color = if (enabled) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(cornerRadius),
        tonalElevation = 6.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 13.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!isBlurred) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Canvas",
                    tint = if (enabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            BasicTextField(
                enabled = enabled,
                value = query,
                onValueChange = onQueryChange,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                decorationBox = { innerTextField ->
                    if (query.text.isEmpty()) {
                        Text(
                            text = "Search Scribbles",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                if (enabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    innerTextField()
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        onSearch()
                    }
                ),
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester)
                    .onFocusChanged {
                        onFocusChange(it.isFocused)
                    }
            )

            Spacer(modifier = Modifier.width(12.dp))

            if (isBlurred) {
                IconButton(
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.surface,
                            CircleShape
                        )
                        .size(28.dp),
                    onClick = onCloseSearchBar
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Search Canvas",
                        modifier = Modifier
                            .padding(6.dp),
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }

    }
}

@Preview
@Composable
fun CanvasSearchBarPreview() {
    var query by remember { mutableStateOf(TextFieldValue("")) }
    var isBlurred by remember { mutableStateOf(true) }
    val focusRequester = remember { FocusRequester() }
    CanvasSearchBar(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        focusRequester = focusRequester,
        query = query,
        enabled = true,
        onQueryChange = {
            query = it
        },
        isBlurred = isBlurred,
        onFocusChange = {
            isBlurred = it
        },
        onSearch = {},
        onCloseSearchBar = {}
    )
}
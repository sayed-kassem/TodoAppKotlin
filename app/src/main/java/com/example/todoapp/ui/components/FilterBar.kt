package com.example.todoapp.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

@Composable
fun FilterBar(
    selectedFilter: String,
    onFilterSelected: (String) -> Unit
) {
    val filters = listOf("All", "Active", "Completed")
    var previousIndex by remember { mutableStateOf(filters.indexOf(selectedFilter)) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        filters.forEachIndexed { index, filter ->
            FilterButton(
                text = filter,
                selected = selectedFilter == filter,
                slideFromLeft = index > previousIndex,
                onClick = {
                    previousIndex = filters.indexOf(selectedFilter)
                    onFilterSelected(filter)
                }
            )
        }
    }
}

@Composable
fun FilterButton(
    text: String,
    selected: Boolean,
    slideFromLeft: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.primary else Color.White,
        animationSpec = tween(durationMillis = 300),
        label = "buttonBackgroundColor"
    )

    val textColor by animateColorAsState(
        targetValue = if (selected) Color.White else MaterialTheme.colorScheme.primary,
        animationSpec = tween(durationMillis = 300),
        label = "buttonTextColor"
    )

    val borderWidth by animateDpAsState(
        targetValue = if (selected) 0.dp else 1.dp,
        animationSpec = tween(durationMillis = 300),
        label = "borderWidth"
    )

    val offsetAnimation by animateFloatAsState(
        targetValue = if (selected) 1f else 0f,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "offsetAnimation"
    )

    var buttonSize by remember { mutableStateOf(IntSize.Zero) }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .border(
                width = borderWidth,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(14.dp)
            )
            .clickable { onClick() }
            .onGloballyPositioned { coordinates -> buttonSize = coordinates.size }
            .drawBehind {
                if (selected) {
                    val radius = 14.dp.toPx()
                    val width = buttonSize.width.toFloat()
                    val height = buttonSize.height.toFloat()
                    val drawWidth = width * offsetAnimation

                    val topLeft = if (slideFromLeft) {
                        Offset(0f, 0f)
                    } else {
                        Offset(width - drawWidth, 0f)
                    }

                    clipRect {
                        drawRoundRect(
                            color = backgroundColor,
                            topLeft = topLeft,
                            size = Size(drawWidth, height),
                            cornerRadius = CornerRadius(radius, radius)
                        )
                    }
                }
            }
            .background(Color.Transparent)
            .padding(vertical = 8.dp, horizontal = 18.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

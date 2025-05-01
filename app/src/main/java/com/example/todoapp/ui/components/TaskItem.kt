package com.example.todoapp.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.todoapp.data.Task
import kotlinx.coroutines.delay

@Composable
fun TaskItem(
    task: Task,
    onDelete: (Task) -> Unit,
    onCheckChanged: (Task, Boolean) -> Unit
) {
    var isDeleting by remember { mutableStateOf(false) }
    var itemVisible by remember { mutableStateOf(true) }

    val dismissState = remember { mutableStateOf(DismissState.Idle) }
    val animatedOffset by animateFloatAsState(
        targetValue = when (dismissState.value) {
            DismissState.DismissedLeft -> -500f
            DismissState.DismissedRight -> 500f
            else -> 0f
        },
        animationSpec = tween(durationMillis = 300),
        label = "offset"
    )
    val animatedAlpha by animateFloatAsState(
        targetValue = when (dismissState.value) {
            DismissState.Idle -> 1f
            else -> 0f
        },
        animationSpec = tween(durationMillis = 300),
        label = "alpha"
    )

    LaunchedEffect(isDeleting) {
        if (isDeleting) {
            itemVisible = false
            delay(300) // Delay to match the animation duration
            onDelete(task)
        }
    }
    AnimatedVisibility(
        visible = itemVisible,
        exit = slideOutHorizontally(
            targetOffsetX = { if (animatedOffset > 0) it else -it },
            animationSpec = tween(300)
        ) + fadeOut(animationSpec = tween(300))
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp,
                pressedElevation = 8.dp
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = task.completed,
                    onCheckedChange = { onCheckChanged(task, it) },
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primary,
                        uncheckedColor = MaterialTheme.colorScheme.outline,
                        checkmarkColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier.padding(end = 16.dp)
                )

                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = if (task.completed) MaterialTheme.colorScheme.onSurfaceVariant
                        else MaterialTheme.colorScheme.onSurface,
                        textDecoration = if (task.completed) TextDecoration.LineThrough
                        else TextDecoration.None
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )

                IconButton(
                    onClick = { isDeleting = true },
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = MaterialTheme.colorScheme.errorContainer,
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Task",
                        tint = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }
    }
}

enum class DismissState{
    Idle,
    DismissedLeft,
    DismissedRight
}
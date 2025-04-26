package com.example.todoapp.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.todoapp.data.Task

@Composable
fun TaskItem(
    task: Task,
    onDelete: (Task)-> Unit,
    onCheckChanged: (Task, Boolean) -> Unit
){
    Card (
        modifier = Modifier.fillMaxWidth().padding(8.dp),
    ) {
        Row (
            modifier = Modifier .padding(16.dp).fillMaxWidth()
        ) {
            Checkbox(
                checked = task.completed,
                onCheckedChange = {onCheckChanged(task, it)},
                modifier = Modifier.padding(end = 16.dp)
            )
            Text(
                text = task.title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = {onDelete(task)},
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.Delete,
                    contentDescription = "Delete Task",
                    tint = Color.Red

                )
            }
        }
    }
}
package com.example.todoapp.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton


@Composable
fun AddTaskDialog(
    onDismiss : () -> Unit,
    onAdd: (String) -> Unit,
    onTextChanged : (String) -> Unit,
    currentText: String
){
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Task") },
        text = {
            OutlinedTextField(
                value= currentText,
                onValueChange = onTextChanged,
                label = {Text("Task Title")},
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onAdd(currentText) },
                enabled = currentText.isNotBlank()
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import com.example.todoapp.data.Task
import com.example.todoapp.data.TaskViewModel
import com.example.todoapp.screens.AddTaskDialog
import com.example.todoapp.ui.components.FilterBar
import com.example.todoapp.ui.components.FloatingButton
import com.example.todoapp.ui.components.TaskItem
import java.util.UUID

// ui/screens/TaskListScreen.kt
@Composable
fun TaskListScreen() {
    val viewModel: TaskViewModel = viewModel()
    val showDialog = remember { mutableStateOf(false) }
    val newTaskTitle = remember { mutableStateOf("") }

    val selectedFilter = viewModel.selectedFilter.collectAsState()

    // Filter bar state
    val filterBarvisible = remember { mutableStateOf(false) }
    val dragOffset = remember { mutableFloatStateOf(0f) }
    val filterBarHeight = 60.dp
    var threshold = with(LocalDensity.current) {filterBarHeight.toPx()}
    val tasks by viewModel.allTasks.collectAsState(initial = emptyList())

    val animatedAlpha by animateFloatAsState(
        targetValue = if (filterBarvisible.value) 1f else 0f,
        animationSpec = tween(durationMillis = 500),
        label = "alphaAnimation"
    )

    Scaffold (
        floatingActionButton = {
            FloatingButton(
                onClick = { showDialog.value = true }
            )
        }
    ){
        innerPadding ->
        Column (
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectVerticalDragGestures { change, dragAmount ->
                        dragOffset.floatValue += dragAmount
                        change.consume()
                        filterBarvisible.value = dragOffset.floatValue >= threshold
                    }
                }
        ){
            AnimatedVisibility(
                visible = filterBarvisible.value,
                enter = slideInVertically(
                    initialOffsetY = { -it },
                    animationSpec = tween(durationMillis = 500)
                ) + fadeIn(animationSpec = tween(durationMillis = 500)),
                exit = slideOutVertically(
                    targetOffsetY = { -it },
                    animationSpec = tween(durationMillis = 500)
                )
            ) {

                FilterBar(
                    selectedFilter = selectedFilter.value,
                    onFilterSelected = viewModel::setSelectedFilter
                )
            }
                LazyColumn {
                    items(
                        items = tasks.filter { task ->
                            when (selectedFilter.value) {
                                "All" -> true
                                "Active" -> !task.completed
                                "Completed" -> task.completed
                                else -> true // default case
                            }
                        }
                    ) { task: Task ->
                        TaskItem(
                            task = task,
                            onDelete = { viewModel.delete(it) },
                            onCheckChanged = { task, checked ->
                                viewModel.update(task.copy(completed = checked))
                            }
                        )
                    }
                }



            // Add task dialog
            if (showDialog.value) {
                AddTaskDialog(
                    onDismiss = { showDialog.value = false },
                    onAdd = { text ->
                        if (text.isNotBlank()) {
                            viewModel.insert(
                                Task(
                                    id = UUID.randomUUID().toString(),
                                    title = text
                                )
                            )
                            newTaskTitle.value = ""
                            showDialog.value = false
                        }
                    },
                    currentText = newTaskTitle.value,
                    onTextChanged = { newTaskTitle.value = it }
                )
            }
        }
    }
}

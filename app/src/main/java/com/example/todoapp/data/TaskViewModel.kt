package com.example.todoapp.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TaskViewModel (application: Application): AndroidViewModel(application) {
    private val repository: TaskRepository
    val allTasks: Flow<List<Task>>

    init {
        val dao = AppDatabase.getDatabase(application).taskDao()
        repository = TaskRepository(dao)
        allTasks = repository.allTasks

    }
    fun insert(task: Task) = viewModelScope.launch {
        repository.insert(task)
    }

    fun delete(task: Task) = viewModelScope.launch {
        repository.delete(task)
    }
    fun update(task: Task) = viewModelScope.launch {
        repository.update(task)
    }

    private val _selectedFilter = MutableStateFlow("All")
    val selectedFilter: StateFlow<String> = _selectedFilter
    fun setSelectedFilter(filter: String) {
        _selectedFilter.value = filter
    }




}
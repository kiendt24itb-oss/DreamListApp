package com.example.todolistapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistapp.model.Task
import com.example.todolistapp.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TaskViewModel : ViewModel() {

    private val repository = TaskRepository()

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading


    fun loadTasks(accountId: Int) {
        viewModelScope.launch {
            _loading.value = true

            try {
                _tasks.value = repository.getTasks(accountId)
            } catch (e: Exception) {
                _tasks.value = emptyList()
            }

            _loading.value = false
        }
    }


    fun addTask(task: Task) {
        viewModelScope.launch {
            repository.addTask(task)
            loadTasks(task.account_id)
        }
    }
}
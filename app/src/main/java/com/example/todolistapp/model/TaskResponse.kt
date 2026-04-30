package com.example.todolistapp.model

data class TaskResponse(
    val success: Boolean,
    val tasks: List<Task>
)
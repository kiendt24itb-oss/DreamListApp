package com.example.todolistapp.model

data class Task(
    val task_id: Int,
    val account_id: Int,
    val title: String,
    val description: String?,
    val status: String,
    // "pending", "in_progress", "completed"

    val due_date: String,
    // backend trả dạng String (yyyy-MM-dd HH:mm:ss)

    val document_link: String?,
    val location: String?
)
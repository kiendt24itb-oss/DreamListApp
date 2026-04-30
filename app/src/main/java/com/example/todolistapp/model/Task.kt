package com.example.todolistapp.model

data class Task(
    val task_id: Int,
    val account_id: Int,
    val title: String,
    val description: String?,
    val status: String,
    val due_date: String,
    val document_link: String?,
    val location: String?
)
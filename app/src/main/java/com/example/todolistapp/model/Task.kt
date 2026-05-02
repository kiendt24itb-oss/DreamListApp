package com.example.todolistapp.model

data class Task(
    val task_id: Int = 0, // Mặc định là 0 cho các task mới tạo ở Client
    val account_id: Int,
    val title: String,
    val description: String? = null,
    val status: String = "pending",
    val due_date: String,
    val document_link: String? = null,
    val location: String? = null
)
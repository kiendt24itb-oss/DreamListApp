package com.example.todolistapp.model

data class RegisterRequest(
    val full_name: String,
    val email: String,
    val password: String
)
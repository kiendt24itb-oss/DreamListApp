package com.example.todolistapp.model

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val account: Account?
)


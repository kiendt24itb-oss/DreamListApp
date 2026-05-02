package com.example.todolistapp.model

data class UserProfile(
    val profile_id: Int,
    val account_id: Int,
    val age: Int?,
    val address: String?,
    val avatar_path: String?,
    val notification_enabled: Boolean,
    val full_name: String? = null,
    val email: String? = null
)
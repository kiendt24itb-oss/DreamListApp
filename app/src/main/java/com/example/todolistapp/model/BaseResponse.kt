package com.example.todolistapp.model

data class BaseResponse(
    val success: Boolean,
    val message: String,
    // Thêm task_id vào đây, để null mặc định vì không phải API nào cũng trả về ID
    val task_id: Int? = null
)
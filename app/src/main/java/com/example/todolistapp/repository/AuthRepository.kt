package com.example.todolistapp.repository

import com.example.todolistapp.model.LoginResponse
import com.example.todolistapp.model.LoginRequest
import com.example.todolistapp.model.RegisterRequest
import com.example.todolistapp.model.BaseResponse
import com.example.todolistapp.network.RetrofitClient

class AuthRepository {

    private val api = RetrofitClient.getClient()

    suspend fun login(email: String, password: String): LoginResponse {
        return api.login(
            LoginRequest(email, password)
        )
    }

    suspend fun register(
        fullName: String,
        email: String,
        password: String
    ): BaseResponse {
        return api.register(
            RegisterRequest(fullName, email, password)
        )
    }
}
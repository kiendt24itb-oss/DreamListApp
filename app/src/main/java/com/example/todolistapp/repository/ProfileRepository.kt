package com.example.todolistapp.repository

import com.example.todolistapp.model.BaseResponse
import com.example.todolistapp.model.ProfileResponse
import com.example.todolistapp.model.UserProfile
import com.example.todolistapp.network.RetrofitClient

class ProfileRepository {

    private val api = RetrofitClient.getClient()

    suspend fun getProfile(accountId: Int): UserProfile {
        val response: ProfileResponse = api.getProfile(mapOf("account_id" to accountId))
        return response.profile ?: throw Exception(response.message ?: "Không tải được profile")
    }

    suspend fun updateProfile(profile: UserProfile): BaseResponse {
        return api.updateProfile(profile)
    }
}
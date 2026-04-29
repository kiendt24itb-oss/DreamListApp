package com.example.todolistapp.repository

import com.example.todolistapp.model.UserProfile
import com.example.todolistapp.model.BaseResponse
import com.example.todolistapp.network.RetrofitClient

class ProfileRepository {

    private val api = RetrofitClient.getClient()

    suspend fun getProfile(accountId: Int): UserProfile {
        return api.getProfile(accountId)
    }

    suspend fun updateProfile(profile: UserProfile): BaseResponse {
        return api.updateProfile(profile)
    }
}
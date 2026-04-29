package com.example.todolistapp.repository

import com.example.todolistapp.model.ExploreLink
import com.example.todolistapp.network.RetrofitClient

class ExploreRepository {

    private val api = RetrofitClient.getClient()

    suspend fun getExploreLinks(): List<ExploreLink> {
        return api.getExploreLinks()
    }
}
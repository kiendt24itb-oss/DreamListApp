package com.example.todolistapp.repository

import com.example.todolistapp.model.Task
import com.example.todolistapp.model.BaseResponse
import com.example.todolistapp.network.RetrofitClient

class TaskRepository {

    private val api = RetrofitClient.getClient()

    suspend fun getTasks(accountId: Int): List<Task> {
        return api.getTasks(accountId)
    }

    suspend fun addTask(task: Task): BaseResponse {
        return api.addTasks(task)
    }

    suspend fun updateTask(task: Task): BaseResponse {
        return api.updateTask(task)
    }

    suspend fun deleteTask(taskId: Int): BaseResponse {
        return api.deleteTask(taskId)
    }
}
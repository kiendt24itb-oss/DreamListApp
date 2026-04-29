package com.example.todolistapp.network

import com.example.todolistapp.model.BaseResponse
import com.example.todolistapp.model.ExploreLink
import com.example.todolistapp.model.LoginRequest
import com.example.todolistapp.model.LoginResponse
import com.example.todolistapp.model.RegisterRequest
import com.example.todolistapp.model.Task
import com.example.todolistapp.model.UserProfile
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    // ================= AUTH =================

    @POST("auth/login.php")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): BaseResponse

    // ================= TASK =================

    @GET("tasks/get_tasks.php")
    suspend fun getTasks(
        @Query("account_id") accountId: Int
    ): List<Task>

    @POST("tasks/add_tasks.php")
    suspend fun addTasks(
        @Body task: Task
    ): BaseResponse

    @POST("tasks/update_task.php")
    suspend fun updateTask(
        @Body task: Task
    ): BaseResponse

    @POST("tasks/delete_task.php")
    suspend fun deleteTask(
        @Query("task_id") taskId: Int
    ): BaseResponse

    // ================= PROFILE =================

    @GET("profile/get_profile.php")
    suspend fun getProfile(
        @Query("account_id") accountId: Int
    ): UserProfile

    @POST("profile/update_profile.php")
    suspend fun updateProfile(
        @Body profile: UserProfile
    ): BaseResponse


    // ================= EXPLORE =================

    @GET("explore/get_explore_links.php")
    suspend fun getExploreLinks(): List<ExploreLink>
}
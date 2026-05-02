package com.example.todolistapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistapp.model.BaseResponse
import com.example.todolistapp.model.LoginResponse
import com.example.todolistapp.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()

    // trạng thái loading
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    // kết quả login
    private val _loginResult = MutableStateFlow<LoginResponse?>(null)
    val loginResult: StateFlow<LoginResponse?> = _loginResult

    // kết quả register
    private val _registerResult = MutableStateFlow<BaseResponse?>(null)
    val registerResult: StateFlow<BaseResponse?> = _registerResult

    // lỗi
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error


    fun login(email: String, password: String) {

        viewModelScope.launch {
            try {
                _loading.value = true

                val response = repository.login(email, password)

                _loginResult.value = response

                if (!response.success) {
                    _error.value = response.message
                }

            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun register(fullName: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null

                val response = repository.register(fullName, email, password)
                _registerResult.value = response

                if (!response.success) {
                    _error.value = response.message
                }

            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }
}

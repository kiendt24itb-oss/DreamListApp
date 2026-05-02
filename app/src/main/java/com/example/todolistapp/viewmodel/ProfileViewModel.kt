package com.example.todolistapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistapp.model.UserProfile
import com.example.todolistapp.model.BaseResponse
import com.example.todolistapp.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val repository = ProfileRepository()

    private val _profile = MutableStateFlow<UserProfile?>(null)
    val profile: StateFlow<UserProfile?> = _profile

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    private val _isError = MutableStateFlow(false)
    val isError: StateFlow<Boolean> = _isError


    fun getProfile(accountId: Int) {
        viewModelScope.launch {
            _loading.value = true
            _isError.value = false

            try {
                _profile.value = repository.getProfile(accountId)
            } catch (e: Exception) {
                _message.value = "Lỗi load profile"
                _isError.value = true
            }

            _loading.value = false
        }
    }


    fun updateProfile(profile: UserProfile) {
        viewModelScope.launch {
            try {
                val response: BaseResponse = repository.updateProfile(profile)
                _message.value = response.message
                _isError.value = !response.success

                if (response.success) {
                    _profile.value = profile
                }

            } catch (e: Exception) {
                _message.value = e.message
                _isError.value = true
            }
        }
    }
}
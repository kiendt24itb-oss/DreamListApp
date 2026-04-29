package com.example.todolistapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistapp.model.ExploreLink
import com.example.todolistapp.repository.ExploreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ExploreViewModel : ViewModel() {

    private val repository = ExploreRepository()

    private val _links = MutableStateFlow<List<ExploreLink>>(emptyList())
    val links: StateFlow<List<ExploreLink>> = _links

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading


    fun loadLinks() {
        viewModelScope.launch {
            _loading.value = true

            try {
                _links.value = repository.getExploreLinks()
            } catch (e: Exception) {
                _links.value = emptyList()
            }

            _loading.value = false
        }
    }
}
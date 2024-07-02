package com.example.gnammy.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gnammy.data.local.entities.Gnam
import com.example.gnammy.data.repository.GnamRepository
import com.example.gnammy.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


data class GnamsState(val gnams: List<Gnam> = emptyList())

class GnamViewModel(private val repository: GnamRepository) : ViewModel() {

    val state = repository.gnams.map { GnamsState(gnams = it) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = GnamsState(emptyList())
    )

    private val _currentGnamId = MutableStateFlow("")
    val currentGnamId: StateFlow<String> = _currentGnamId.asStateFlow()

    private val _isInitialized = MutableStateFlow(false)
    val isInitialized: StateFlow<Boolean> = _isInitialized.asStateFlow()

    private val _loginState = MutableStateFlow<Result<String>?>(null)
    val loginState: StateFlow<Result<String>?> = _loginState

    init {
        viewModelScope.launch {
            repository.currentGnamId.collect { gnamId ->
                _currentGnamId.value = gnamId
                _isInitialized.value = true
            }
        }
    }

    fun fetchGnam(gnamId: String) {
        viewModelScope.launch {
            repository.fetchGnam(gnamId)
        }
    }

    fun fetchGnams() {
        viewModelScope.launch {
            repository.fetchGnams()
        }
    }
}

package com.example.gnammy.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gnammy.data.local.entities.User
import com.example.gnammy.data.repository.UserRepository
import com.example.gnammy.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


data class UsersState(val users: List<User> = emptyList())

class UserViewModel(private val repository: UserRepository) : ViewModel() {

    val state = repository.users.map { UsersState(users = it) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = UsersState(emptyList())
    )

    private val _currentUserId = MutableStateFlow("")
    val currentUserId: StateFlow<String> = _currentUserId.asStateFlow()

    private val _isInitialized = MutableStateFlow(false)
    val isInitialized: StateFlow<Boolean> = _isInitialized.asStateFlow()

    private val _loginState = MutableStateFlow<Result<String>?>(null)
    val loginState: StateFlow<Result<String>?> = _loginState

    init {
        viewModelScope.launch {
            repository.currentUserId.collect { userId ->
                _currentUserId.value = userId
                _isInitialized.value = true
            }
        }
    }

    fun fetchUser(userId: String) {
        viewModelScope.launch {
            repository.fetchUser(userId)
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginState.value = repository.login(username, password)
        }
    }

    fun register(username: String, password: String) {
        viewModelScope.launch {
            repository.register(username, password)
        }
    }
}

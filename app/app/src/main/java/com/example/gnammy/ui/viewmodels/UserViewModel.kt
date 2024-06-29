package com.example.gnammy.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gnammy.data.local.entities.User
import com.example.gnammy.data.repository.UserRepository
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

    init {
        viewModelScope.launch {
            repository.currentUserId.collect { userId ->
                _currentUserId.value = userId
                _isInitialized.value = true
            }
        }
    }

    fun setUserId(value: String) {
        viewModelScope.launch {
            repository.setUser(value)
        }
    }

    fun fetchUser(userId: String) = viewModelScope.launch {
            repository.getUser(userId)
    }

//    fun addUser(user: User, image: MultipartBody.Part?) {
//        viewModelScope.launch {
//            repository.addUser(user, image) {
//                it.fold(
//                    onSuccess = { /* Gestisci il successo */ },
//                    onFailure = { /* Gestisci l'errore */ }
//                )
//            }
//        }
//    }
//
//    fun changeUserInfo(userId: String, user: User, image: MultipartBody.Part?) {
//        viewModelScope.launch {
//            repository.changeUserInfo(userId, user, image) {
//                it.fold(
//                    onSuccess = { /* Gestisci il successo */ },
//                    onFailure = { /* Gestisci l'errore */ }
//                )
//            }
//        }
//    }
}

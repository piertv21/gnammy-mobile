package com.example.gnammy.ui.viewmodels

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gnammy.data.local.entities.User
import com.example.gnammy.data.repository.UserRepository
import com.example.gnammy.utils.Coordinates
import com.example.gnammy.utils.Result
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
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

    suspend fun getLoggedUserId(): String {
        return repository.loggedUserId.first()
    }

    fun fetchUser(userId: String) {
        viewModelScope.launch {
            repository.fetchUser(userId)
        }
    }

    suspend fun login(username: String, password: String): Result<String> {
        return repository.login(username, password)
    }

    suspend fun register(
        context: Context, username: String, password: String, profilePictureUri: Uri
    ): Result<String> {
        return repository.register(context, username, password, profilePictureUri)
    }

    fun updateUserLocation(coordinates: Coordinates) {
        viewModelScope.launch {
            repository.updateUserLocation(coordinates, getLoggedUserId())
        }
    }
}

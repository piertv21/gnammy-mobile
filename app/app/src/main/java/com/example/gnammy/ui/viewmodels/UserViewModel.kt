package com.example.gnammy.ui.viewmodels

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gnammy.data.local.entities.User
import com.example.gnammy.data.repository.UserRepository
import com.example.gnammy.ui.theme.Themes
import com.example.gnammy.utils.Coordinates
import com.example.gnammy.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    var loggedUserId: String = "NOT SET"

    val homeBtnEnabled = mutableStateOf(false)

    init {
        viewModelScope.launch {
            homeBtnEnabled.value = repository.homeBtnEnabled.first()
        }
    }

    fun toggleHomeBtn() {
        viewModelScope.launch {
            repository.toggleHomeBtnEnabled()
            homeBtnEnabled.value = repository.homeBtnEnabled.first()
        }
    }

    suspend fun getLoggedUserId(): String {
        loggedUserId = repository.loggedUserId.first()
        return loggedUserId
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

    fun updateUserData(
        context: Context, username: String, profilePictureUri: Uri?
    ) {
        viewModelScope.launch {
            repository.updateUserData(context, username, profilePictureUri, getLoggedUserId())
        }
    }

    suspend fun logout() {
        repository.clearData()
    }
}

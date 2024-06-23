package com.example.gnammy.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gnammy.data.local.entities.User
import com.example.gnammy.data.repository.UserRepository
import kotlinx.coroutines.flow.SharingStarted
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

//    fun fetchUsers() {
//        viewModelScope.launch {
//            repository.listUsers {
//                it.fold(
//                    onSuccess = { usersList -> users.value = usersList },
//                    onFailure = { /* Gestisci l'errore */ }
//                )
//            }
//        }
//    }

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

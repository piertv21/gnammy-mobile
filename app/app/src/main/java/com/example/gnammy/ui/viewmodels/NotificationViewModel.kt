package com.example.gnammy.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gnammy.data.local.entities.Notification
import com.example.gnammy.data.repository.NotificationRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


data class NotificationState(val notifications: List<Notification> = emptyList())

class NotificationViewModel(private val repository: NotificationRepository) : ViewModel() {

    val state = repository.notifications.map { NotificationState(notifications = it) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = NotificationState(emptyList())
    )

    fun fetchNotifications(userId: String) {
        viewModelScope.launch {
            repository.fetchNotifications(userId)
        }
    }

    suspend fun setAsSeen(notificationId: String) {
        repository.setAsSeen(notificationId)
    }
}

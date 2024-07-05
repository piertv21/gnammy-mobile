package com.example.gnammy.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gnammy.data.local.entities.Notification
import com.example.gnammy.data.repository.NotificationRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


data class NotificationState(val notifications: List<Notification> = emptyList())

class NotificationViewModel(private val repository: NotificationRepository) : ViewModel() {

    val state = repository.notifications.map { NotificationState(notifications = it) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = NotificationState(emptyList())
    )

    suspend fun fetchNotifications(userId: String) {
        repository.fetchNotifications(userId)
    }

    suspend fun setAsSeen(notificationId: String) {
        repository.setAsSeen(notificationId)
    }
}

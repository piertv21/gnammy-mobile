package com.example.gnammy.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: String,
    val username: String,
    val password: String,
    val location: String?,
    val imageUri: String?

    // val followers: List<Following>,
    // val following: List<Following>,
    // val gnams: List<Gnam>,
    // val likes: List<Like>,
    // val sentNotifications: List<Notification>,
    // val receivedNotifications: List<Notification>,
    // val goals: List<Goal>
)

package com.example.gnammy.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class Notification(
    @PrimaryKey val id: String,
    val gnamId: String?,
    val sourceId: String,
    val imageUri: String,
    val content: String,
    val createdAt: Long
)

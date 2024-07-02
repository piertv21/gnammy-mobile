package com.example.gnammy.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: String,
    val username: String,
    val location: String?,
    val imageUri: String?,
    val followers: Int,
    val following: Int,
)

package com.example.gnammy.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_goals")
data class UserGoal(
    @PrimaryKey val id: String,
    val userId: String,
    val content: String,
    val imageUri: String
)

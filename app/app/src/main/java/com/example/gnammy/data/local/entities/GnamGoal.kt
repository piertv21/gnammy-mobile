package com.example.gnammy.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gnam_goals")
data class GnamGoal(
    @PrimaryKey val id: String,
    val userId: String,
    val gnamId: String,
    val content: String,
    val imageUri: String
)

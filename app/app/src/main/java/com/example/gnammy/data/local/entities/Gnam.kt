package com.example.gnammy.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gnams")
data class Gnam(
    @PrimaryKey val id: String,
    val title: String,
    val authorId: String,
    val imageUri: String,
    val date: Long,
    val description: String,
    val recipe: String,
    val authorImageUri: String,
    val authorName: String
)

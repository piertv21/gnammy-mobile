package com.example.gnammy.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(
    tableName = "liked_gnams",
    foreignKeys = [ForeignKey(
        entity = Gnam::class,
        parentColumns = ["id"],
        childColumns = ["gnamId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class LikedGnam(
    @PrimaryKey val gnamId: String
)
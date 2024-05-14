package com.example.gnammy.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Place (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo
    val name: String,

    @ColumnInfo
    val date: String,

    @ColumnInfo
    val description: String,

    @ColumnInfo
    val imageUri: String?
)

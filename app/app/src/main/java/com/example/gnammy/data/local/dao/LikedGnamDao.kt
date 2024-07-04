package com.example.gnammy.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Delete
import androidx.room.Query
import androidx.room.OnConflictStrategy
import com.example.gnammy.data.local.entities.LikedGnam

@Dao
interface LikedGnamDao {
    @Insert
    suspend fun insertLikedGnam(likedGnam: LikedGnam)

    @Delete
    suspend fun deleteLikedGnam(likedGnam: LikedGnam)

    @Insert
    suspend fun insertAll(likedGnams: List<LikedGnam>)

    @Delete
    suspend fun deleteAll()

    @Query("SELECT * FROM liked_gnams")
    suspend fun getAllLikedGnams(): List<LikedGnam>
}
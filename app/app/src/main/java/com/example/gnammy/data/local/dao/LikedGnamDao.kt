package com.example.gnammy.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Delete
import androidx.room.Query
import androidx.room.OnConflictStrategy
import com.example.gnammy.data.local.entities.Gnam
import com.example.gnammy.data.local.entities.LikedGnam
import kotlinx.coroutines.flow.Flow

@Dao
interface LikedGnamDao {
    @Insert
    suspend fun insertLikedGnam(likedGnam: LikedGnam)

    @Query("DELETE FROM liked_gnams WHERE gnamId = :gnamId")
    suspend fun deleteLikedGnam(gnamId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(likedGnams: List<LikedGnam>)

    @Query("DELETE FROM liked_gnams")
    suspend fun deleteAll()

    @Query("SELECT * FROM gnams WHERE id IN (SELECT gnamId FROM liked_gnams)")
    fun getAllLikedGnams(): Flow<List<Gnam>>
}
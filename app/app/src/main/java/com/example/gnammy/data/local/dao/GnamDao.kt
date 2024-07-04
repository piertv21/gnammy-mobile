package com.example.gnammy.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.gnammy.data.local.entities.Gnam
import kotlinx.coroutines.flow.Flow

@Dao
interface GnamDao {
    @Query("SELECT * FROM gnams")
    fun getAllGnams(): Flow<List<Gnam>>

    @Query("SELECT * FROM gnams WHERE id = :gnamId")
    suspend fun getUserById(gnamId: String): Gnam

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(gnams: List<Gnam>)

    @Upsert
    suspend fun upsert(gnam: Gnam)

    @Query("DELETE FROM gnams WHERE id = :gnamId")
    suspend fun delete(gnamId: String)

    @Query("SELECT * FROM gnams WHERE id IN (:gnamIds)")
    suspend fun getAllLikedGnams(gnamIds: List<String>): List<Gnam>
}

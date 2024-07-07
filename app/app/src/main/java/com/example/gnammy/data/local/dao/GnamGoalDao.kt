package com.example.gnammy.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gnammy.data.local.entities.GnamGoal
import kotlinx.coroutines.flow.Flow

@Dao
interface GnamGoalDao {
    @Query("SELECT * FROM gnam_goals")
    fun getGoals(): Flow<List<GnamGoal>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(goals: List<GnamGoal>)
}

package com.example.gnammy.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gnammy.data.local.entities.UserGoal
import kotlinx.coroutines.flow.Flow

@Dao
interface UserGoalDao {
    @Query("SELECT * FROM user_goals")
    fun getGoals(): Flow<List<UserGoal>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(goals: List<UserGoal>)
}

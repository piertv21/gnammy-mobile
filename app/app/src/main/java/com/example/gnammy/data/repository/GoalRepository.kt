package com.example.gnammy.data.repository

import android.util.Log
import com.example.gnammy.backendSocket
import com.example.gnammy.data.local.dao.GnamGoalDao
import com.example.gnammy.data.local.dao.UserGoalDao
import com.example.gnammy.data.local.entities.GnamGoal
import com.example.gnammy.data.local.entities.UserGoal
import com.example.gnammy.data.remote.RetrofitClient
import com.example.gnammy.data.remote.apis.GoalApiService
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException

class GoalRepository(
    private val gnamGoalDao: GnamGoalDao,
    private val userGoalDao: UserGoalDao
) {
    private val apiService: GoalApiService =
        RetrofitClient.instance.create(GoalApiService::class.java)

    val userGoals: Flow<List<UserGoal>> = userGoalDao.getGoals()
    val gnamGoals: Flow<List<GnamGoal>> = gnamGoalDao.getGoals()

    suspend fun fetchGoals(userId: String) {
        try {
            val goalsResponse = apiService.getGoals(userId)

            if (goalsResponse.isSuccessful) {
                val goalsBody = goalsResponse.body()
                if (goalsBody != null) {
                    val userGoalList: MutableList<UserGoal> = mutableListOf()
                    val gnamGoalList: MutableList<GnamGoal> = mutableListOf()
                    goalsBody.goals.forEach() {
                        if (it.gnamId == null) {
                            userGoalList.add(
                                UserGoal(
                                    id = it.id,
                                    userId = it.userId,
                                    content = it.content,
                                    imageUri = "${backendSocket}/images/user/${it.userId}.jpg",
                                )
                            )
                        } else {
                            gnamGoalList.add(
                                GnamGoal(
                                    id = it.id,
                                    userId = it.userId,
                                    gnamId = it.gnamId!!,
                                    content = it.content,
                                    imageUri = "${backendSocket}/images/gnam/${it.gnamId}.jpg",
                                )
                            )
                        }
                    }
                    userGoalDao.insertAll(userGoalList)
                    gnamGoalDao.insertAll(gnamGoalList)
                }
            } else {
                Log.e("GnamRepository", "Error in getting gnam: ${goalsResponse.message()}")
            }
        } catch (e: IOException) {
            Log.e("GnamRepository", "Network error in getting gnam", e)
        } catch (e: HttpException) {
            Log.e("GnamRepository", "HTTP error in getting gnam", e)
        }
    }

    suspend fun getGoalsPreview(userId: String): List<UserGoal> {
        try {
            val goalsResponse = apiService.getGoalsPreview(userId, 5)

            if (goalsResponse.isSuccessful) {
                val goalsBody = goalsResponse.body()
                if (goalsBody != null) {
                    val userGoalList: MutableList<UserGoal> = mutableListOf()

                    goalsBody.goals.forEach() {
                        userGoalList.add(
                            UserGoal(
                                id = it.id,
                                userId = it.userId,
                                content = it.content,
                                imageUri = "${backendSocket}/images/user/${it.id}.jpg",
                            )
                        )
                    }

                    return userGoalList
                }
            } else {
                Log.e("GnamRepository", "Error in getting gnam: ${goalsResponse.message()}")
            }
        } catch (e: IOException) {
            Log.e("GnamRepository", "Network error in getting gnam", e)
        } catch (e: HttpException) {
            Log.e("GnamRepository", "HTTP error in getting gnam", e)
        }

        return emptyList()
    }
}

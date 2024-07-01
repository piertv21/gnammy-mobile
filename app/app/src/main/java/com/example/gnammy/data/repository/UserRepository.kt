package com.example.gnammy.data.repository

import android.content.ContentResolver
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.gnammy.data.local.dao.UserDao
import com.example.gnammy.data.local.entities.User
import com.example.gnammy.data.remote.RetrofitClient
import com.example.gnammy.data.remote.apis.UserApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException

class UserRepository(
    private val userDao: UserDao,
    private val dataStore: DataStore<Preferences>
) {
    private val apiService: UserApiService = RetrofitClient.instance.create(UserApiService::class.java)

    companion object {
        private val USER_ID_KEY = stringPreferencesKey("user_id_key")
    }

    val currentUserId = dataStore.data.map { it[USER_ID_KEY] ?: "" }

    suspend fun setUser(value: String) = dataStore.edit { it[USER_ID_KEY] = value }

    val users: Flow<List<User>> = userDao.getAllUsers()

//    suspend fun addUser(user: User) {
//        try {
//            val response = apiService.addUser(user, user.imageUri)
//            if (response.isSuccessful) {
//                response.body()?.let {
//                    userDao.upsert(it.toUser())
//                }
//            } else {
//                Log.e("UserRepository", "Error in adding user: ${response.message()}")
//            }
//        } catch (e: IOException) {
//            Log.e("UserRepository", "Network error in adding user", e)
//        } catch (e: HttpException) {
//            Log.e("UserRepository", "HTTP error in adding user", e)
//        }
//    }

//    suspend fun listUsers(): Result<List<User>> {
//        return try {
//            val response = apiService.listUsers()
//            if (response.isSuccessful) {
//                Result.success(response.body()?.map { it.toUser() } ?: emptyList())
//            } else {
//                Result.failure(Throwable(response.message()))
//            }
//        } catch (e: IOException) {
//            Result.failure(e)
//        } catch (e: HttpException) {
//            Result.failure(e)
//        }
//    }

    suspend fun getUser(userId: String) {
        try {
            val userResponse = apiService.getUser(userId)

            if (userResponse.isSuccessful) {
                val userRes = userResponse.body()

                if (userRes != null) {
                    userRes.user?.let { userDao.upsert(it.toUser()) }
                }
            } else {
                Log.e("UserRepository", "Error in getting user: ${userResponse.message()}")
            }
        } catch (e: IOException) {
            Log.e("UserRepository", "Network error in getting user", e)
        } catch (e: HttpException) {
            Log.e("UserRepository", "HTTP error in getting user", e)
        }
    }

    suspend fun login(username: String, password: String) {
        try {
            val response = apiService.getUser(username)
            if (response.isSuccessful) {
                response.body()?.let {
                    userDao.upsert(it.toUser())
                }
            } else {
                Log.e("UserRepository", "Error in login: ${response.message()}")
            }
        } catch (e: IOException) {
            Log.e("UserRepository", "Network error in login", e)
        } catch (e: HttpException) {
            Log.e("UserRepository", "HTTP error in login", e)
        }
    }

//    suspend fun changeUserInfo(userId: String, user: User, image: MultipartBody.Part?): Result<User> {
//        return try {
//            val response = apiService.changeUserInfo(userId, user, image)
//            if (response.isSuccessful) {
//                Result.success(response.body() ?: user)
//            } else {
//                Result.failure(Throwable(response.message()))
//            }
//        } catch (e: IOException) {
//            Result.failure(e)
//        } catch (e: HttpException) {
//            Result.failure(e)
//        }
//    }
}

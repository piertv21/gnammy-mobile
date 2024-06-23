package com.example.gnammy.data.repository

import android.content.ContentResolver
import android.util.Log
import com.example.gnammy.data.local.dao.UserDao
import com.example.gnammy.data.local.entities.User
import com.example.gnammy.data.remote.RetrofitClient
import com.example.gnammy.data.remote.apis.UserApiService
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException

class UserRepository(
    private val userDao: UserDao,
    private val contentResolver: ContentResolver
) {
    private val apiService: UserApiService = RetrofitClient.instance.create(UserApiService::class.java)

    val users: Flow<List<User>> = userDao.getAllUsers()
// gestione immagine
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

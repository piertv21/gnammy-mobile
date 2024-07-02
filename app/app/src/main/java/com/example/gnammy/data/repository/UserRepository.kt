package com.example.gnammy.data.repository

import android.content.ContentResolver
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.gnammy.backendSocket
import com.example.gnammy.data.local.dao.UserDao
import com.example.gnammy.data.local.entities.User
import com.example.gnammy.data.remote.RetrofitClient
import com.example.gnammy.data.remote.apis.UserCredentials
import com.example.gnammy.data.remote.apis.UserApiService
import com.example.gnammy.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException

class UserRepository(
    private val userDao: UserDao,
    private val contentResolver: ContentResolver,
    private val dataStore: DataStore<Preferences>
) {
    private val apiService: UserApiService = RetrofitClient.instance.create(UserApiService::class.java)

    companion object {
        private val USER_ID_KEY = stringPreferencesKey("user_id_key")
    }

    val currentUserId = dataStore.data.map { it[USER_ID_KEY] ?: "" }

    suspend fun setUser(value: String) = dataStore.edit { it[USER_ID_KEY] = value }

    val users: Flow<List<User>> = userDao.getAllUsers()

    suspend fun fetchUser(userId: String) {
        try {
            val userResponse = apiService.getUser(userId)

            if (userResponse.isSuccessful) {
                val followerResponse = apiService.getFollowers(userId)
                val followingResponse = apiService.getFollowing(userId)
                var followers = 0
                var following = 0

                if (followerResponse.isSuccessful) {
                    followers = followerResponse.body()?.followers?.size ?: 0
                }
                if (followingResponse.isSuccessful) {
                    following = followingResponse.body()?.following?.size ?: 0
                }

                val userRes = userResponse.body()
                if (userRes != null) {
                    userRes.user?.let {
                        val user = User(it.id, it.username, it.location,
                            "$backendSocket/images/user/${it.id}.jpg", followers, following)
                        userDao.upsert(user)
                    }
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

    suspend fun login(username: String, password: String): Result<String> {
        return try {
            val response = apiService.login(UserCredentials(username, password))

            if (response.isSuccessful) {
                val userResponse = response.body()?.user
                if (userResponse != null) {
                    fetchUser(userResponse.id)
                    setUser(userResponse.id)
                    Result.Success("Login success for user: ${userResponse.username}")
                } else {
                    Result.Error("Empty user received in login response")
                }
            } else {
                Result.Error("Errore: credenziali errate")
            }
        } catch (e: IOException) {
            Result.Error("Network error in login")
        } catch (e: HttpException) {
            Result.Error("HTTP error in login")
        }
    }

    suspend fun register(username: String, password: String) {
        try {
            // convert profilePictureUri to multipart body
            val response = apiService.addUser(UserCredentials(username, password), null) // TODO: Add image

            if (response.isSuccessful) {
                val userResponse = response.body()?.user
                if (userResponse != null) {
                    //userDao.upsert(userResponse.toUser())
                    setUser(userResponse.id)
                    Log.d("UserRepository", "Register success for user: ${userResponse.username}")
                } else {
                    Log.e("UserRepository", "Empty user received in register response")
                }
            } else {
                Log.e("UserRepository", "Error in register: ${response.message()}")
            }
        } catch (e: IOException) {
            Log.e("UserRepository", "Network error in register", e)
        } catch (e: HttpException) {
            Log.e("UserRepository", "HTTP error in register", e)
        }
    }
}

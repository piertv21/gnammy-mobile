package com.example.gnammy.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.gnammy.backendSocket
import com.example.gnammy.data.local.GnammyDatabase
import com.example.gnammy.data.local.dao.UserDao
import com.example.gnammy.data.local.entities.User
import com.example.gnammy.data.remote.RetrofitClient
import com.example.gnammy.data.remote.apis.ToggleFollowRequest
import com.example.gnammy.data.remote.apis.UserApiService
import com.example.gnammy.data.remote.apis.UserCredentials
import com.example.gnammy.utils.Coordinates
import com.example.gnammy.utils.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.java.KoinJavaComponent.inject
import retrofit2.HttpException
import java.io.File
import java.io.IOException

class UserRepository(
    private val userDao: UserDao,
    private val dataStore: DataStore<Preferences>
) {
    private val apiService: UserApiService =
        RetrofitClient.instance.create(UserApiService::class.java)

    companion object {
        private val USER_ID_KEY = stringPreferencesKey("user_id_key")
        private val HOME_BTN_ENABLED_KEY = booleanPreferencesKey("home_btn_enabled_key")
    }

    val loggedUserId = dataStore.data.map { it[USER_ID_KEY] ?: "NOT SET" }

    suspend fun setUser(value: String) = dataStore.edit { it[USER_ID_KEY] = value }

    val homeBtnEnabled = dataStore.data.map { it[HOME_BTN_ENABLED_KEY] ?: true }

    suspend fun toggleHomeBtnEnabled() {
        dataStore.edit { preferences ->
            val currentValue = preferences[HOME_BTN_ENABLED_KEY] ?: true
            preferences[HOME_BTN_ENABLED_KEY] = !currentValue
        }
    }

    val users: Flow<List<User>> = userDao.getAllUsers()

    suspend fun clearData() {
        val database: GnammyDatabase by inject(GnammyDatabase::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            database.clearAllTables()
        }
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

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
                        val user = User(
                            it.id, it.username, it.location,
                            "$backendSocket/images/user/${it.imageUri}", followers, following
                        )
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
                    Result.Success("Login avvenuto con successo! Redirect...")
                } else {
                    Result.Error("Empty user received in login response")
                }
            } else {
                Result.Error("Errore: credenziali errate")
            }
        } catch (e: IOException) {
            Result.Error("Errore: connessione di rete assente")
        } catch (e: HttpException) {
            Result.Error("HTTP error in login")
        }
    }

    suspend fun register(
        context: Context,
        username: String,
        password: String,
        profilePictureUri: Uri
    ): Result<String> {
        return try {
            val usernamePart = username.toRequestBody("text/plain".toMediaTypeOrNull())
            val passwordPart = password.toRequestBody("text/plain".toMediaTypeOrNull())

            val imagePart: MultipartBody.Part? = profilePictureUri?.let { uri ->
                val file = File(context.cacheDir, "tempProfilePicture")
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    file.outputStream().use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
                val requestFile = RequestBody.create(
                    context.contentResolver.getType(uri)?.toMediaTypeOrNull(),
                    file
                )
                MultipartBody.Part.createFormData("image", file.name, requestFile)
            }

            val response = apiService.addUser(usernamePart, passwordPart, imagePart)

            if (response.isSuccessful) {
                val userResponse = response.body()?.user
                if (userResponse != null) {
                    fetchUser(userResponse.id)
                    setUser(userResponse.id)
                    Result.Success("Registrazione avvenuta con successo! Redirect...")
                } else {
                    Result.Error("Empty user received in register response")
                }
            } else {
                Result.Error("Error in register: ${response.message()}")
            }
        } catch (e: IOException) {
            Result.Error("Errore: connessione di rete assente")
        } catch (e: HttpException) {
            Result.Error("HTTP error in register")
        }
    }

    suspend fun updateUserLocation(coordinates: Coordinates, userId: String) {
        try {
            val url =
                "https://nominatim.openstreetmap.org/reverse?lat=${coordinates.latitude}&lon=${coordinates.longitude}&format=json&limit=1"
            val placeName = apiService.getPlaceName(url)
            if (placeName.isSuccessful) {
                val city = placeName.body()?.address?.city
                val town = placeName.body()?.address?.town
                val country = placeName.body()?.address?.country
                val location = if (city != null) {
                    "$city, $country"
                } else if (town != null) {
                    "$town, $country"
                } else {
                    "Unknown location"
                }
                val response = apiService.changeUserInfo(
                    userId,
                    null,
                    null,
                    location.let { RequestBody.create("text/plain".toMediaTypeOrNull(), it) },
                    null
                )
                if (response.isSuccessful) {
                    fetchUser(userId)
                } else {
                    Log.e(
                        "UserRepository",
                        "Errore nell'aggiornamento delle informazioni dell'utente: ${response.message()}"
                    )
                }
            } else {
                Log.e("UserRepository", "Error in getting place name: ${placeName.message()}")
            }
        } catch (e: IOException) {
            Log.e("UserRepository", "Network error in updating user location", e)
        } catch (e: HttpException) {
            Log.e("UserRepository", "HTTP error in updating user location", e)
        }
    }

    suspend fun updateUserData(
        context: Context,
        username: String,
        profilePictureUri: Uri?,
        userId: String
    ) {
        try {
            val usernamePart = username.toRequestBody("text/plain".toMediaTypeOrNull())

            var imagePart: MultipartBody.Part? = null
            if(profilePictureUri != null) {
                imagePart = profilePictureUri.let { uri ->
                    val file = File(context.cacheDir, "tempProfilePicture")
                    context.contentResolver.openInputStream(uri)?.use { inputStream ->
                        file.outputStream().use { outputStream ->
                            inputStream.copyTo(outputStream)
                        }
                    }
                    val requestFile = file
                        .asRequestBody(uri.let {
                            context.contentResolver.getType(it)?.toMediaTypeOrNull()
                        })
                    MultipartBody.Part.createFormData("image", file.name, requestFile)
                }
            }

            val response = apiService.changeUserInfo(userId, usernamePart, null, null, imagePart)
            if (response.isSuccessful) {
                fetchUser(userId)
            } else {
                Log.e(
                    "UserRepository",
                    "Errore nell'aggiornamento delle informazioni dell'utente: ${response.message()}"
                )
            }
        } catch (e: IOException) {
            Log.e("UserRepository", "Network error in updating user data", e)
        } catch (e: HttpException) {
            Log.e("UserRepository", "HTTP error in updating user data", e)
        }
    }

    suspend fun followUser(userId: String, currentUserId: String): Result<String> {
        return try {
            val response = apiService.toggleFollowUser(ToggleFollowRequest(currentUserId, userId))
            if (response.isSuccessful) {
                Result.Success(response.body()?.result?.followed.toString())
            } else {
                Result.Error("Errore nel seguire l'utente: ${response.message()}")
            }
        } catch (e: IOException) {
            Result.Error("Network error in following user")
        } catch (e: HttpException) {
            Result.Error("HTTP error in following user")
        }
    }

    suspend fun isUserFollowing(userId: String, currentUserId: String): Result<String> {
        return try {
            val response = apiService.isUserFollowing(currentUserId, userId)
            if (response.isSuccessful) {
                Result.Success(response.body()?.result.toString())
            } else {
                Result.Error("Errore nel verificare se l'utente è seguito: ${response.message()}")
            }
        } catch (e: IOException) {
            Result.Error("Network error in checking if user is following")
        } catch (e: HttpException) {
            Result.Error("HTTP error in checking if user is following")
        }
    }
}
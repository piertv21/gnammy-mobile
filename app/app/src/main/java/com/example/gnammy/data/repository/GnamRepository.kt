package com.example.gnammy.data.repository

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.gnammy.backendSocket
import com.example.gnammy.data.local.dao.GnamDao
import com.example.gnammy.data.local.dao.LikedGnamDao
import com.example.gnammy.data.local.entities.Gnam
import com.example.gnammy.data.local.entities.LikedGnam
import com.example.gnammy.data.remote.RetrofitClient
import com.example.gnammy.data.remote.apis.GnamApiService
import com.example.gnammy.data.remote.apis.LikeRequest
import com.example.gnammy.utils.DateFormats
import com.example.gnammy.utils.Result
import com.example.gnammy.utils.dateStringToMillis
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File
import java.io.IOException

class GnamRepository(
    private val gnamDao: GnamDao,
    private val likedGnamDao: LikedGnamDao,
    private val contentResolver: ContentResolver,
    private val dataStore: DataStore<Preferences>
) {
    private val apiService: GnamApiService =
        RetrofitClient.instance.create(GnamApiService::class.java)

    val gnams: Flow<List<Gnam>> = gnamDao.getAllGnams()
    val timeline: MutableStateFlow<List<Gnam>> = MutableStateFlow(emptyList())

    companion object {
        private val TIMELINE_OFFSET_KEY = intPreferencesKey("timeline_offset_key")
        private val USER_ID_KEY = stringPreferencesKey("user_id_key")
    }

    val timeline_offset = dataStore.data.map { it[TIMELINE_OFFSET_KEY] ?: 0 }

    suspend fun setTimelineOffsetKey(value: Int) =
        dataStore.edit { it[TIMELINE_OFFSET_KEY] = value }


    private suspend fun getCurrentUserId(): String {
        return dataStore.data.map { it[USER_ID_KEY] ?: "" }.first()
    }

    suspend fun getGnamData(gnamId: String): Gnam? {
        return try {
            val gnamResponse = apiService.getGnam(gnamId)
            if (gnamResponse.isSuccessful) {
                val gnamRes = gnamResponse.body()
                if (gnamRes != null) {
                    gnamRes.gnam?.let {
                        val gnam = Gnam(
                            id = it.id,
                            authorId = it.authorId,
                            title = it.title,
                            description = it.description,
                            recipe = it.recipe,
                            date = dateStringToMillis(it.createdAt, DateFormats.DB_FORMAT),
                            imageUri = "${backendSocket}/images/gnam/${it.id}.jpg",
                            authorImageUri = "${backendSocket}/images/user/${it.authorImageUri}",
                            authorName = it.authorName
                        )
                        return gnam
                    }
                }
                return null
            } else {
                return null
            }
        } catch (e: IOException) {
            return null
        } catch (e: HttpException) {
            return null
        }
    }

    suspend fun fetchGnam(gnamId: String) {
        try {
            val gnamResponse = apiService.getGnam(gnamId)

            if (gnamResponse.isSuccessful) {
                val gnamRes = gnamResponse.body()
                if (gnamRes != null) {
                    gnamRes.gnam?.let {
                        val gnam = Gnam(
                            id = it.id,
                            authorId = it.authorId,
                            title = it.title,
                            description = it.description,
                            recipe = it.recipe,
                            date = dateStringToMillis(it.createdAt, DateFormats.DB_FORMAT),
                            imageUri = "${backendSocket}/images/gnam/${it.id}.jpg",
                            authorImageUri = "${backendSocket}/images/user/${it.authorImageUri}",
                            authorName = it.authorName
                        )
                        gnamDao.upsert(gnam)
                    }
                }
            } else {
                Log.e("GnamRepository", "Error in getting gnam: ${gnamResponse.message()}")
            }
        } catch (e: IOException) {
            Log.e("GnamRepository", "Network error in getting gnam", e)
        } catch (e: HttpException) {
            Log.e("GnamRepository", "HTTP error in getting gnam", e)
        }
    }

    suspend fun fetchGnamTimeline() {
        try {
            val offset: Int = timeline_offset.first()
            val currentUserId = getCurrentUserId()
            val gnamResponse = apiService.getGnamTimeline(currentUserId, offset)
            setTimelineOffsetKey(offset + 10)

            if (gnamResponse.isSuccessful) {
                val gnamRes = gnamResponse.body()
                val listGnams: MutableList<Gnam> = timeline.value.toMutableList()
                gnamRes?.gnams?.forEach() {
                    listGnams.add(
                        Gnam(
                            id = it.id,
                            authorId = it.authorId,
                            title = it.title,
                            description = it.description,
                            recipe = it.recipe,
                            date = dateStringToMillis(it.createdAt, DateFormats.DB_FORMAT),
                            imageUri = "${backendSocket}/images/gnam/${it.id}.jpg",
                            authorImageUri = "${backendSocket}/images/user/${it.authorImageUri}",
                            authorName = it.authorName
                        )
                    )
                }
                timeline.value = listGnams
            } else {
                Log.e("GnamRepository", "Error in getting gnam: ${gnamResponse.message()}")
            }
        } catch (e: IOException) {
            Log.e("GnamRepository", "Network error in getting gnam", e)
        } catch (e: HttpException) {
            Log.e("GnamRepository", "HTTP error in getting gnam", e)
        }
    }

    suspend fun publishGnam(
        context: Context,
        currentUserId: String,
        title: String,
        shortDescription: String,
        ingredientsAndRecipe: String,
        imageUri: Uri
    ): Result<String> {
        return try {
            val authorIdPart = currentUserId.toRequestBody("text/plain".toMediaTypeOrNull())
            val titlePart = title.toRequestBody("text/plain".toMediaTypeOrNull())
            val descriptionPart = shortDescription.toRequestBody("text/plain".toMediaTypeOrNull())
            val recipePart = ingredientsAndRecipe.toRequestBody("text/plain".toMediaTypeOrNull())

            val imagePart: MultipartBody.Part? = imageUri.let { uri ->
                val file = File(context.cacheDir, "tempGnamImage")
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

            val response =
                apiService.addGnam(authorIdPart, titlePart, descriptionPart, recipePart, imagePart)

            if (response.isSuccessful) {
                val gnamResponse = response.body()?.gnam
                if (gnamResponse != null) {
                    Result.Success("Gnam pubblicato con successo!")
                } else {
                    Result.Error("Empty response received in publishGnam")
                }
            } else {
                Result.Error("Error in publishGnam: ${response.message()}")
            }
        } catch (e: IOException) {
            Result.Error("Network error in publishGnam")
        } catch (e: HttpException) {
            Result.Error("HTTP error in publishGnam")
        }
    }

    suspend fun likeGnam(gnam: Gnam) {
        try {
            val response = apiService.likeGnam(LikeRequest(gnam.id, getCurrentUserId()))
            if (response.isSuccessful) {
                gnamDao.upsert(gnam)
                likedGnamDao.insertLikedGnam(LikedGnam(gnam.id))
            } else {
                Log.e("GnamRepository", "Error in liking gnam: ${response.message()}")
            }
        } catch (e: IOException) {
            Log.e("GnamRepository", "Network error in liking gnam", e)
        } catch (e: HttpException) {
            Log.e("GnamRepository", "HTTP error in liking gnam", e)
        }
    }

    suspend fun removeFromTimeline(gnam: Gnam, liked: Boolean) {
        if (liked) {
            try {
                likeGnam(gnam)
            } catch (e: IOException) {
                Log.e("GnamRepository", "Network error in removing like", e)
            } catch (e: HttpException) {
                Log.e("GnamRepository", "HTTP error in removing like", e)
            }
        }
        val currentTimeline = timeline.value.toMutableList()
        currentTimeline.remove(gnam)
        timeline.value = currentTimeline
        Log.i("GnamRepository", "timeline has now ${timeline.value.size} elements")
    }

    suspend fun addCurrentUserGnams(userId: String) {
        try {
            val response = apiService.getUserGnams(userId)
            if (response.isSuccessful) {
                val gnamRes = response.body()
                gnamRes?.gnams?.forEach {
                    gnamDao.upsert(
                        Gnam(
                            id = it.id,
                            authorId = it.authorId,
                            title = it.title,
                            description = it.description,
                            recipe = it.recipe,
                            date = dateStringToMillis(it.createdAt, DateFormats.DB_FORMAT),
                            imageUri = "${backendSocket}/images/gnam/${it.id}.jpg",
                            authorImageUri = "${backendSocket}/images/user/${it.authorImageUri}",
                            authorName = it.authorName
                        )
                    )
                }
            } else {
                Log.e("GnamRepository", "Error in getting user gnams: ${response.message()}")
            }
        } catch (e: IOException) {
            Log.e("GnamRepository", "Network error in getting user gnams", e)
        } catch (e: HttpException) {
            Log.e("GnamRepository", "HTTP error in getting user gnams", e)
        }
    }
}

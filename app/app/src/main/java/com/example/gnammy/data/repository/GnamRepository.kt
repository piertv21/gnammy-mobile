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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.gnammy.backendSocket
import com.example.gnammy.data.local.dao.GnamDao
import com.example.gnammy.data.local.dao.LikedGnamDao
import com.example.gnammy.data.local.entities.Gnam
import com.example.gnammy.data.local.entities.LikedGnam
import com.example.gnammy.data.remote.RetrofitClient
import com.example.gnammy.data.remote.apis.GnamApiService
import com.example.gnammy.utils.DateFormats
import com.example.gnammy.utils.dateStringToMillis
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.example.gnammy.utils.Result
import kotlinx.coroutines.flow.first
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
    private val apiService: GnamApiService = RetrofitClient.instance.create(GnamApiService::class.java)

    companion object {
        private val TIMELINE_OFFSET_KEY = intPreferencesKey("timeline_offset_key")
        private val USER_ID_KEY = stringPreferencesKey("user_id_key")
    }

    val timeline_offset = dataStore.data.map { it[TIMELINE_OFFSET_KEY] ?: 0 }

    suspend fun setTimelineOffsetKey(value: Int) = dataStore.edit { it[TIMELINE_OFFSET_KEY] = value }

    val gnams: Flow<List<Gnam>> = gnamDao.getAllGnams()

    private suspend fun getCurrentUserId(): String {
        return dataStore.data.map { it[USER_ID_KEY] ?: "" }.first()
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
                            imageUri = "backendSocket/images/gnam/${it.id}.jpg")
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
                val listGnams: MutableList<Gnam> = mutableListOf()
                gnamRes?.gnams?.forEach() {
                    listGnams.add(Gnam(
                        id = it.id,
                        authorId = it.authorId,
                        title = it.title,
                        description = it.description,
                        recipe = it.recipe,
                        date = dateStringToMillis(it.createdAt, DateFormats.DB_FORMAT),
                        imageUri = "${backendSocket}/images/gnam/${it.id}.jpg"))
                }
                gnamDao.insertAll(listGnams)
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

            val response = apiService.addGnam(authorIdPart, titlePart, descriptionPart, recipePart, imagePart)

            if (response.isSuccessful) {
                val gnamResponse = response.body()?.gnam
                if (gnamResponse != null) {
                    Result.Success("Gnam published successfully with ID: ${gnamResponse.id}")
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

    suspend fun syncSavedGnam(userId: String): Result<String> {
        return try {
            val backendGnams = apiService.getSavedGnams(userId)

            if (backendGnams.isSuccessful) {
                val gnamRes = backendGnams.body()
                val listGnams: MutableList<Gnam> = mutableListOf()
                gnamRes?.gnams?.forEach() {
                    listGnams.add(Gnam(
                        id = it.id,
                        authorId = it.authorId,
                        title = it.title,
                        description = it.description,
                        recipe = it.recipe,
                        date = dateStringToMillis(it.createdAt, DateFormats.DB_FORMAT),
                        imageUri = "${backendSocket}/images/gnam/${it.id}.jpg"))
                }
                gnamDao.insertAll(listGnams)

                val likedGnamsIds = listGnams.map { it.id }
                likedGnamDao.insertAll(likedGnamsIds.map { LikedGnam(it) })

                Result.Success("Saved gnams synced successfully")
            } else {
                Result.Error("Error in getting saved gnam: ${backendGnams.message()}")
            }
        } catch (e) {
            Result.Error("Network error in publishGnam")
        }
    }
}

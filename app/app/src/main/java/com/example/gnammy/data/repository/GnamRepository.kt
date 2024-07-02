package com.example.gnammy.data.repository

import android.content.ContentResolver
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.gnammy.backendSocket
import com.example.gnammy.data.local.dao.GnamDao
import com.example.gnammy.data.local.entities.Gnam
import com.example.gnammy.data.remote.RetrofitClient
import com.example.gnammy.data.remote.apis.GnamApiService
import com.example.gnammy.utils.DateFormats
import com.example.gnammy.utils.dateStringToMillis
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException

class GnamRepository(
    private val gnamDao: GnamDao,
    private val contentResolver: ContentResolver,
    private val dataStore: DataStore<Preferences>
) {
    private val apiService: GnamApiService = RetrofitClient.instance.create(GnamApiService::class.java)

    companion object {
        private val USER_ID_KEY = stringPreferencesKey("gnam_id_key")
    }

    val currentGnamId = dataStore.data.map { it[USER_ID_KEY] ?: "" }

    suspend fun setGnam(value: String) = dataStore.edit { it[USER_ID_KEY] = value }

    val gnams: Flow<List<Gnam>> = gnamDao.getAllGnams()

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

    suspend fun fetchGnams() {
        try {
            val gnamResponse = apiService.listGnams()

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
}
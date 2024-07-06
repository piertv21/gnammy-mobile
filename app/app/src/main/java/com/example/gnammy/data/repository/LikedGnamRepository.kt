package com.example.gnammy.data.repository

import android.content.ContentResolver
import com.example.gnammy.backendSocket
import com.example.gnammy.data.local.dao.GnamDao
import com.example.gnammy.data.local.dao.LikedGnamDao
import com.example.gnammy.data.local.entities.Gnam
import com.example.gnammy.data.local.entities.LikedGnam
import com.example.gnammy.data.remote.RetrofitClient
import com.example.gnammy.data.remote.apis.GnamApiService
import com.example.gnammy.utils.DateFormats
import com.example.gnammy.utils.Result
import com.example.gnammy.utils.dateStringToMillis
import kotlinx.coroutines.flow.Flow

class LikedGnamRepository(
    private val gnamDao: GnamDao,
    private val likedGnamDao: LikedGnamDao,
    private val contentResolver: ContentResolver
) {
    private val apiService: GnamApiService =
        RetrofitClient.instance.create(GnamApiService::class.java)

    val likedGnams: Flow<List<Gnam>> = likedGnamDao.getAllLikedGnams()

    suspend fun syncSavedGnam(userId: String): Result<String> {
        return try {
            val backendGnams = apiService.getSavedGnams(userId)

            if (backendGnams.isSuccessful) {
                val gnamRes = backendGnams.body()
                val listGnams: MutableList<Gnam> = mutableListOf()
                gnamRes?.gnams?.forEach() {
                    listGnams.add(
                        Gnam(
                            id = it.id,
                            authorId = it.authorId,
                            title = it.title,
                            description = it.description,
                            recipe = it.recipe,
                            date = dateStringToMillis(it.createdAt, DateFormats.DB_FORMAT),
                            imageUri = "$backendSocket/images/gnam/${it.id}.jpg",
                            authorImageUri = "$backendSocket/images/user/${it.authorId}.jpg",
                            authorName = it.authorName
                        )
                    )
                }
                gnamDao.insertAll(listGnams)

                val likedGnamsIds = listGnams.map { it.id }
                likedGnamDao.insertAll(likedGnamsIds.map { LikedGnam(it) })

                Result.Success("Saved gnams synced successfully")
            } else {
                Result.Error("Error in getting saved gnam: ${backendGnams.message()}")
            }
        } catch (e: Exception) {
            Result.Error("Network error in publishGnam")
        }
    }
}
package com.example.gnammy.data.repository

import android.content.ContentResolver
import com.example.gnammy.data.local.dao.GnamDao
import com.example.gnammy.data.local.dao.LikedGnamDao
import com.example.gnammy.data.local.entities.Gnam
import com.example.gnammy.data.local.entities.LikedGnam

class LikedGnamRepository(
    private val gnamDao: GnamDao,
    private val likedGnamDao: LikedGnamDao,
    private val contentResolver: ContentResolver
) {
    suspend fun likeGnam(gnam: Gnam) {
        //TODO backend comunication

        gnamDao.upsert(gnam)
        val likedGnam = LikedGnam(gnamId = gnam.id)
        likedGnamDao.insertLikedGnam(likedGnam)
    }

    suspend fun dislikeGnam(gnam: Gnam) {
        //TODO backend comunication

        gnamDao.delete(gnam.id)
        val likedGnam = LikedGnam(gnamId = gnam.id)
        likedGnamDao.deleteLikedGnam(likedGnam)
    }

    suspend fun getAllLikedGnams(): List<Gnam> {
        val likedGnamsIds = likedGnamDao.getAllLikedGnams().map { it.gnamId }
        return gnamDao.getAllLikedGnams(likedGnamsIds)
    }

    suspend fun insertAllLikedGnams(likedGnams: List<Gnam>) {
        //TODO backend comunication (likedGnams: List<Gnam> verrà tolto)

        likedGnams.forEach { likedGnam ->
            gnamDao.upsert(likedGnam)
            val likedGnamEntity = LikedGnam(gnamId = likedGnam.id)
            likedGnamDao.insertLikedGnam(likedGnamEntity)
        }
    }

    suspend fun deleteAll() {
        likedGnamDao.deleteAll()
    }
}
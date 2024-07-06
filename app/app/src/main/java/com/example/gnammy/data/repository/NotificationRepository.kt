package com.example.gnammy.data.repository

import android.util.Log
import com.example.gnammy.backendSocket
import com.example.gnammy.data.local.dao.GnamDao
import com.example.gnammy.data.local.dao.NotificationDao
import com.example.gnammy.data.local.entities.Notification
import com.example.gnammy.data.remote.RetrofitClient
import com.example.gnammy.data.remote.apis.NotificationApiService
import com.example.gnammy.utils.DateFormats
import com.example.gnammy.utils.dateStringToMillis
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException

class NotificationRepository(
    private val gnamDao: GnamDao,
    private val notificationDao: NotificationDao,
) {
    private val apiService: NotificationApiService =
        RetrofitClient.instance.create(NotificationApiService::class.java)
    
    val notifications: Flow<List<Notification>> = notificationDao.getAllNotification()

    suspend fun fetchNotifications(userId: String) {
        try {
            val notificationResponse = apiService.getNewNotifications(userId)

            if (notificationResponse.isSuccessful) {
                val notificationBody = notificationResponse.body()
                if (notificationBody != null) {
                    val notificationList: MutableList<Notification> = mutableListOf()
                    notificationBody.notifications?.forEach() {
                        val imageUri = if (it.gnamId.isNotEmpty()) {
                            "$backendSocket/images/gnam/${it.gnamId}.jpg"
                        } else {
                            "$backendSocket/images/user/${it.sourceId}.jpg"
                        }
                        val notification = Notification(
                            id = it.id,
                            gnamId = it.gnamId.ifEmpty { null },
                            sourceId = it.sourceId,
                            content = it.content,
                            createdAt = dateStringToMillis(it.createdAt, DateFormats.DB_FORMAT),
                            imageUri = imageUri
                        )
                        notificationList.add(notification)
                    }
                    notificationDao.insertAll(notificationList)
                }
            } else {
                Log.e("GnamRepository", "Error in getting gnam: ${notificationResponse.message()}")
            }
        } catch (e: IOException) {
            Log.e("GnamRepository", "Network error in getting gnam", e)
        } catch (e: HttpException) {
            Log.e("GnamRepository", "HTTP error in getting gnam", e)
        }
    }

    suspend fun setAsSeen(notificationId: String) {
        try {
            val response = apiService.setAsSeen(notificationId)

            if (response.isSuccessful) {
                val notificationResponse = response.body()?.result
                notificationDao.delete(notificationId)
                Log.i("NotificationRepository", "Notification seen: $notificationResponse")
            } else {
                Log.e(
                    "NotificationRepository",
                    "Error in setting notification as seen: ${response.message()}"
                )
            }
        } catch (e: IOException) {
            Log.e("NotificationRepository", "Network error in setting notification as seen", e)
        } catch (e: HttpException) {
            Log.e("NotificationRepository", "HTTP error in setting notification as seen", e)
        }
    }
}

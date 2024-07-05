package com.example.gnammy.data.remote.apis

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

class NotificationResponse {
    var id: String = ""
    var gnamId: String = ""
    var sourceId: String = ""
    var content: String = ""
    var createdAt: String = ""
}

class NotificationWrapper {
    var notification: NotificationResponse? = null
}

class NotificationListResponse {
    var notifications: List<NotificationResponse>? = null
}

interface NotificationApiService {
    @POST("/notification/{notificationId}")
    suspend fun setAsSeen(@Path("notificationId") notificationId: String): Response<NotificationWrapper>

    @GET("/notification/{userId}")
    suspend fun getNewNotifications(@Path("userId") userId: String): Response<NotificationListResponse>
}

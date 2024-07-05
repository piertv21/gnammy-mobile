package com.example.gnammy.data.remote.apis

import com.example.gnammy.utils.ImageResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

class GnamResponse {
    var id: String = ""
    var authorId: String = ""
    var title: String = ""
    var description: String = ""
    var recipe: String = ""
    var shareCount: Int = 0
    var createdAt: String = ""
    var authorName: String = ""
}

class GnamWrapperResponse {
    var gnam: GnamResponse? = null
}

class GnamListWrapperResponse {
    var gnams: List<GnamResponse>? = null
}

interface GnamApiService {
    @Multipart
    @POST("/gnam/")
    suspend fun addGnam(
        @Part("authorId") authorId: RequestBody,
        @Part("title") title: RequestBody,
        @Part("short_description") description: RequestBody,
        @Part("full_recipe") recipe: RequestBody,
        @Part image: MultipartBody.Part?
    ): Response<GnamWrapperResponse>

    @GET("/gnam/timeline/{userId}/{offset}")
    suspend fun getGnamTimeline(
        @Path("userId") userId: String,
        @Path("offset") offset: Int
    ): Response<GnamListWrapperResponse>

    @GET("/gnam/{gnamId}")
    suspend fun getGnam(@Path("gnamId") gnamId: String): Response<GnamWrapperResponse>

    @GET("/image/gnam/{gnamId}")
    suspend fun getGnamImage(@Path("gnamId") gnamId: String): Response<ImageResponse>

    @POST("/share/{gnamId}")
    suspend fun shareGnam(@Path("gnamId") gnamId: String): Response<GnamWrapperResponse>

    @GET("/savedGnams/{userId}")
    suspend fun getSavedGnams(@Path("userId") userId: String): Response<GnamListWrapperResponse>
}

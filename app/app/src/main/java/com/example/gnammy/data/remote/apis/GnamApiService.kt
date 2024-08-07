package com.example.gnammy.data.remote.apis

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

class GnamResponse {
    var id: String = ""
    var authorId: String = ""
    var title: String = ""
    var description: String = ""
    var recipe: String = ""
    var shareCount: Int = 0
    var createdAt: String = ""
    var authorName: String = ""
    var authorImageUri: String = ""
}

class GnamWrapperResponse {
    var gnam: GnamResponse? = null
}

class GnamListWrapperResponse {
    var gnams: List<GnamResponse>? = null
}

class GnamTimelineResponse {
    var gnams: List<GnamResponse>? = null
    var offset: Int = 0
}

class LikeRequest(
    val gnamId: String,
    val userId: String
)

class LikeResponse {
    var id: String = ""
    var gnamId: String = ""
    var userId: String = ""
}

class LikeWrapperResponse {
    var like: LikeResponse? = null
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
    ): Response<GnamTimelineResponse>

    @GET("/gnam/{gnamId}")
    suspend fun getGnam(@Path("gnamId") gnamId: String): Response<GnamWrapperResponse>

    @POST("/share/{gnamId}")
    suspend fun shareGnam(@Path("gnamId") gnamId: String): Response<GnamWrapperResponse>

    @GET("/savedGnams/{userId}")
    suspend fun getSavedGnams(@Path("userId") userId: String): Response<GnamListWrapperResponse>

    @POST("/like/")
    suspend fun likeGnam(@Body request: LikeRequest): Response<LikeWrapperResponse>

    @DELETE("/like/{userId}/{gnamId}")
    suspend fun unlikeGnam(
        @Path("userId") userId: String,
        @Path("gnamId") gnamId: String
    ): Response<LikeWrapperResponse>

    @GET("/gnamsOf/{userId}")
    suspend fun getUserGnams(@Path("userId") userId: String): Response<GnamListWrapperResponse>

    @GET("/search")
    suspend fun searchGnams(
        @Query("userId") userId: String,
        @Query("keywords") keywords: String,
        @Query("dateTo") dateTo: String,
        @Query("dateFrom") dateFrom: String,
        @Query("numberOfLikes") numberOfLikes: Int
    ): Response<GnamListWrapperResponse>

}

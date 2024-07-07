package com.example.gnammy.data.remote.apis

import com.example.gnammy.data.local.entities.User
import com.example.gnammy.utils.ImageResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

class UserResponse {
    var id: String = ""
    var username: String = ""
    var password: String = ""
    var location: String? = null
}

class UserWrapperResponse {
    var user: UserResponse? = null
}

class followersResponse {
    var followers: List<UserResponse>? = null
}

class followingResponse {
    var following: List<UserResponse>? = null
}

data class UserCredentials (
    val username: String,
    val password: String
)

data class UserInfo (
    val username: String?,
    val password: String?,
    val location: String?
)

data class OSMAddress(
    val city: String?
)

data class OSMPlace (
    val address: OSMAddress?
)

interface UserApiService {
    @Multipart
    @POST("/user/")
    suspend fun addUser(
        @Part("username") username: RequestBody,
        @Part("password") password: RequestBody,
        @Part image: MultipartBody.Part?
    ): Response<UserWrapperResponse>

    @POST("/login/")
    suspend fun login(@Body request: UserCredentials): Response<UserWrapperResponse>

    @GET("/user/")
    suspend fun listUsers(): Response<List<UserWrapperResponse>>

    @GET("/user/{userId}")
    suspend fun getUser(@Path("userId") userId: String): Response<UserWrapperResponse>

    @GET("/follower/{userId}/")
    suspend fun getFollowers(@Path("userId") userId: String): Response<followersResponse>

    @GET("/following/{userId}/")
    suspend fun getFollowing(@Path("userId") userId: String): Response<followingResponse>

    @GET("/image/user/{userId}")
    suspend fun getUserImage(@Path("userId") userId: String): Response<ImageResponse>

    @Multipart
    @PATCH("/user/{userId}")
    suspend fun changeUserInfo(
        @Path("userId") userId: String,
        @Part("username") username: RequestBody?,
        @Part("password") password: RequestBody?,
        @Part("location") location: RequestBody?,
        @Part image: MultipartBody.Part?
    ): Response<UserWrapperResponse>

    @GET
    suspend fun getPlaceName(
        @Url url: String
    ): Response<OSMPlace>
}

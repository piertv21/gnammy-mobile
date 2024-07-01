package com.example.gnammy.data.remote.apis

import com.example.gnammy.backendSocket
import com.example.gnammy.data.local.entities.User
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

class ImageResponse {
    var image: String = ""
}

class UserResponse {
    var id: String = ""
    var username: String = ""
    var password: String = ""
    var location: String? = null

    fun toUser(): User {
        return User(id, username, password, location, "$backendSocket/image/user/$id")
    }
}

class UserWrapperResponse {
    var user: UserResponse? = null
}

data class LoginRequest (
    val username: String,
    val password: String
)

interface UserApiService {
    @POST("/user/")
    suspend fun addUser(@Body user: User, @Part image: MultipartBody.Part?): Response<UserWrapperResponse>

    @POST("/login")
    suspend fun login(@Body request: LoginRequest): Response<UserWrapperResponse>

    @GET("/user/")
    suspend fun listUsers(): Response<List<UserWrapperResponse>>

    @GET("/user/{userId}")
    suspend fun getUser(@Path("userId") userId: String): Response<UserWrapperResponse>

    @GET("/image/user/{userId}")
    suspend fun getUserImage(@Path("userId") userId: String): Response<ImageResponse>

    @PATCH("/user/{userId}")
    suspend fun changeUserInfo(@Path("userId") userId: String, @Body user: User, @Part image: MultipartBody.Part?): Response<UserWrapperResponse>
}

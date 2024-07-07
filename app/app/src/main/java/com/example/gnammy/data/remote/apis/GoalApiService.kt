package com.example.gnammy.data.remote.apis

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

class GoalResponse {
    var id: String = ""
    var userId: String = ""
    var content: String = ""
    var gnamId: String? = ""
}

class GoalsWrapperResponse {
    var goals: List<GoalResponse> = emptyList()
}

interface GoalApiService {
    @GET("/goals/{userId}")
    suspend fun getGoals(@Path("userId") userId: String): Response<GoalsWrapperResponse>

    @GET("/goals/{userId}/{limit}")
    suspend fun getGoalsPreview(
        @Path("userId") userId: String,
        @Path("limit") limit: Int
    ): Response<GoalsWrapperResponse>
}

package com.example.myloginapp.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("api/token/")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

}

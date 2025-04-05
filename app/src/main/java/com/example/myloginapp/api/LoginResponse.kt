package com.example.myloginapp.api

data class LoginResponse(
    val access: String, // token
    val refresh: String? // optional refresh token
)

package com.example.irrigationfrontend.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

// Request body data class
data class RegisterRequest(
    val name: String,
    val username: String,
    val password: String
)

data class AuthResponse(
    val token: String?
)

data class LoginRequest(
    val username: String,
    val password: String
)

interface AuthApi {
    @POST("login/saveUser")
    fun registerUser(@Body request: RegisterRequest): Call<okhttp3.ResponseBody>

    @POST("login/authUser")
    fun authUser(@Body request: LoginRequest): Call<AuthResponse>
}

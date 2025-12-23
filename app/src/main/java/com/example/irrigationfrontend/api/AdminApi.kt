package com.example.irrigationfrontend.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

data class AddUserRequest(
    val username: String
)

data class ChangeCommandRequest(
    val username: String,
    val grant: Boolean
)

interface AdminApi {
    @GET("admin/enterRoom")
    fun enterRoom(@Header("Authorization") token: String): Call<ResponseBody>

    @POST("admin/addUser")
    fun addUser(
        @Header("Authorization") token: String,
        @Query("username") username: String
    ): Call<ResponseBody>

    @POST("admin/changeCommand")
    fun changeCommand(
        @Header("Authorization") token: String,
        @Query("username") username: String,
        @Query("value") value: Boolean
    ): Call<ResponseBody>

    @POST("admin/assignScheduler")
    fun assignScheduler(
        @Header("Authorization") token: String,
        @Query("username") username: String,
        @Query("time") time: String,
        @Query("time2") time2: String
    ): Call<ResponseBody>
}

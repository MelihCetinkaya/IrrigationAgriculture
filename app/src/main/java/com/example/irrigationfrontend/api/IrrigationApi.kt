package com.example.irrigationfrontend.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface IrrigationApi {

    @POST("common/factor")
    fun setIrrigationStatus(
        @Header("Authorization") token: String,
        @Query("activity") activity: Boolean,
        @Query("factor") factor: String
    ): Call<ResponseBody>

    @POST("common/threshold")
    fun setThreshold(
        @Header("Authorization") token: String,
        @Query("factor") factor: String,
        @Query("threshold") threshold: String,
        @Query("minmax") minmax: String
    ): Call<ResponseBody>

    @POST("common/irrigation")
    fun setIrrigationTime(
        @Header("Authorization") token: String,
        @Query("time") time: String
    ): Call<ResponseBody>

    @POST("common/minmax")
    fun setMinmaxTime(
        @Header("Authorization") token: String,
        @Query("minmax") minmax: String,
        @Query("factor") factor: String,
        @Query("time") time: Int
    ): Call<ResponseBody>

    @POST("common/notification")
    fun setNotification(
        @Header("Authorization") token: String,
        @Query("notifyType") notifyType: String,
        @Query("activity") activity: Boolean
    ): Call<ResponseBody>
}

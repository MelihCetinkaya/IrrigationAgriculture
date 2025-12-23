package com.example.irrigationfrontend.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ValuesApi {
    @GET("values/enterRoom")
    fun enterValuesRoom(
        @Header("Authorization") token: String
    ): Call<ResponseBody>
    
    @GET("values/showValues")
    fun getTemperature(
        @Header("Authorization") token: String,
        @Query("factor") factor: String
    ): Call<ResponseBody>

    @GET("values/checkFactor")
    fun checkFactor(
        @Header("Authorization") token: String,
        @Query("factor") factor: String
    ): Call<ResponseBody>

    @GET("values/thresholds")
    fun getThreshold(
        @Header("Authorization") token: String,
        @Query("factor") factor: String,
        @Query("minmax") minmax: String
    ): Call<ResponseBody>

    @GET("values/minmaxTime")
    fun getMinmaxTime(
        @Header("Authorization") token: String,
        @Query("factor") factor: String,
        @Query("minmax") minmax: String
    ): Call<ResponseBody>

    @GET("values/notification")
    fun getNotificationStatus(
        @Header("Authorization") token: String,
        @Query("notifType") notifType: String
    ): Call<ResponseBody>
}

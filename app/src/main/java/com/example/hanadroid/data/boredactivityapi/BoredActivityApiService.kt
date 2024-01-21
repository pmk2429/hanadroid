package com.example.hanadroid.data.boredactivityapi

import com.example.hanadroid.model.BoredActivity
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BoredActivityApiService {

    @GET(".")
    suspend fun getRandomBoredActivity(): Response<BoredActivity>

    @GET(".")
    suspend fun getBoredActivityByType(@Query("type") type: String): Response<BoredActivity>
}
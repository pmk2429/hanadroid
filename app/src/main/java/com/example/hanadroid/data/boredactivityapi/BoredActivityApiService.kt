package com.example.hanadroid.data.boredactivityapi

import com.example.hanadroid.data.model.BoredActivity
import retrofit2.http.GET
import retrofit2.http.Query

interface BoredActivityApiService {

    @GET(".")
    suspend fun getRandomBoredActivity(): BoredActivity

    @GET(".")
    suspend fun getBoredActivityByType(@Query("type") type: String): BoredActivity
}
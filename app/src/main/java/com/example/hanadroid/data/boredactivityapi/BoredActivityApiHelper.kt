package com.example.hanadroid.data.boredactivityapi

import com.example.hanadroid.model.BoredActivity
import retrofit2.Response

interface BoredActivityApiHelper {
    suspend fun getRandomBoredActivity(): Response<BoredActivity>

    suspend fun getBoredActivityByType(type: String): Response<BoredActivity>
}
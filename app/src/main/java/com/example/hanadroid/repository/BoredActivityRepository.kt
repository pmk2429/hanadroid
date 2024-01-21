package com.example.hanadroid.repository

import com.example.hanadroid.data.boredactivityapi.BoredActivityApiHelper
import com.example.hanadroid.model.BoredActivity
import com.example.hanadroid.networking.NetworkResult
import com.example.hanadroid.util.CoroutineDispatcherProvider

class BoredActivityRepository(
    private val apiHelper: BoredActivityApiHelper,
    coroutineDispatcherProvider: CoroutineDispatcherProvider
) : BaseRepository(coroutineDispatcherProvider) {
    suspend fun fetchBoredActivityByType(
        type: String
    ): NetworkResult<BoredActivity> {
        return safeApiCall { apiHelper.getBoredActivityByType(type) }
    }

    suspend fun fetchRandomBoredActivity(): NetworkResult<BoredActivity> {
        return safeApiCall { apiHelper.getRandomBoredActivity() }
    }
}

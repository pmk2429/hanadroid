package com.example.hanadroid.data.boredactivityapi

import com.example.hanadroid.model.BoredActivity
import retrofit2.Response

class BoredActivityApiHelperImpl(
    private val boredActivityApiService: BoredActivityApiService
) : BoredActivityApiHelper {
    override suspend fun getRandomBoredActivity(): Response<BoredActivity> =
        boredActivityApiService.getRandomBoredActivity()

    override suspend fun getBoredActivityByType(type: String): Response<BoredActivity> =
        boredActivityApiService.getBoredActivityByType(type)
}

package com.example.hanadroid.data.boredactivityapi

import com.example.hanadroid.model.BoredActivity

class BoredActivityApiHelperImpl(
    private val boredActivityApiService: BoredActivityApiService
) : BoredActivityApiHelper {
    override suspend fun getRandomBoredActivity(): BoredActivity =
        boredActivityApiService.getRandomBoredActivity()

    override suspend fun getBoredActivityByType(type: String): BoredActivity =
        boredActivityApiService.getBoredActivityByType(type)
}

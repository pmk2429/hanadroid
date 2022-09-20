package com.example.hanadroid.ui.repository

import com.example.hanadroid.data.boredactivityapi.BoredActivityApiHelper
import com.example.hanadroid.data.model.BoredActivity
import com.example.hanadroid.networking.ResponseWrapper
import com.example.hanadroid.util.CoroutineDispatcherProvider
import kotlinx.coroutines.withContext

class BoredActivityRepository(
    private val apiHelper: BoredActivityApiHelper,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider
) {
    suspend fun fetchBoredActivityByType(
        type: String
    ): ResponseWrapper<BoredActivity> = withContext(coroutineDispatcherProvider.io()) {
        val result = apiHelper.getBoredActivityByType(type)
        try {
            ResponseWrapper.Success(result)
        } catch (e: Exception) {
            ResponseWrapper.Error(e.message!!)
        }
    }

    suspend fun fetchRandomBoredActivity(): ResponseWrapper<BoredActivity> =
        withContext(coroutineDispatcherProvider.io()) {
            val result = apiHelper.getRandomBoredActivity()
            try {
                ResponseWrapper.Success(result)
            } catch (e: Exception) {
                ResponseWrapper.Error(e.message!!)
            }
        }
}

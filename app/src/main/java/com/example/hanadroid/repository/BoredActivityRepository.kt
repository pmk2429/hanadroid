package com.example.hanadroid.repository

import com.example.hanadroid.data.boredactivityapi.BoredActivityApiHelper
import com.example.hanadroid.model.AppError
import com.example.hanadroid.model.BoredActivity
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

    /**
     * Fetch Random Bored Activity using Kotlin Result API.
     */
    suspend fun fetchRandomBoredActivityForResult(): Result<BoredActivity> =
        withContext(coroutineDispatcherProvider.io()) {
            val data = apiHelper.getRandomBoredActivity()
            try {
                Result.success(data)
            } catch (e: Exception) {
                // Result.failure(e)
                handleError(e)
            }
        }

    private fun <AppError> handleError(t: Throwable): Result<AppError> {
        return Result.failure(AppError(message = t.message, cause = t.cause))
    }
}

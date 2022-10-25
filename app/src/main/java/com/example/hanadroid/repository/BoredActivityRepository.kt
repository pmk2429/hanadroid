package com.example.hanadroid.repository

import com.example.hanadroid.data.boredactivityapi.BoredActivityApiHelper
import com.example.hanadroid.model.AppError
import com.example.hanadroid.model.BoredActivity
import com.example.hanadroid.networking.ResponseWrapper
import com.example.hanadroid.networking.ResponseWrapper.Error
import com.example.hanadroid.networking.ResponseWrapper.Success
import com.example.hanadroid.util.CoroutineDispatcherProvider
import kotlinx.coroutines.withContext
import kotlin.Result.Companion.success

class BoredActivityRepository(
    private val apiHelper: BoredActivityApiHelper,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider
) {
    suspend fun fetchBoredActivityByType(
        type: String
    ): ResponseWrapper<BoredActivity> = withContext(coroutineDispatcherProvider.io()) {
        return@withContext try {
            Success(apiHelper.getBoredActivityByType(type))
        } catch (e: Exception) {
            Error(e.message!!)
        }
    }

    suspend fun fetchRandomBoredActivity(): ResponseWrapper<BoredActivity> =
        withContext(coroutineDispatcherProvider.io()) {
            return@withContext try {
                Success(apiHelper.getRandomBoredActivity())
            } catch (e: Exception) {
                Error(e.message!!)
            }
        }

    /**
     * Fetch Random Bored Activity using Kotlin Result API.
     */
    suspend fun fetchRandomBoredActivityForResult(): Result<BoredActivity> =
        withContext(coroutineDispatcherProvider.io()) {
            return@withContext try {
                success(apiHelper.getRandomBoredActivity())
            } catch (e: Exception) {
                handleError(e)
            }
        }

    private fun <AppError> handleError(t: Throwable): Result<AppError> {
        return Result.failure(AppError(message = t.message, cause = t.cause))
    }
}

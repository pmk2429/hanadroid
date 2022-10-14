package com.example.hanadroid.repository

import com.example.hanadroid.data.dogapi.DogApiHelper
import com.example.hanadroid.model.CeoDog
import com.example.hanadroid.model.WoofDog
import com.example.hanadroid.util.CoroutineDispatcherProvider
import kotlinx.coroutines.withContext

class DogsRepository(
    private val apiHelper: DogApiHelper,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider
) {

    suspend fun getWoofDog(): Result<WoofDog> =
        withContext(coroutineDispatcherProvider.io()) {
            val response = apiHelper.getDogFromWoof()
            try {
                Result.success(response)
            } catch (e: Exception) {
                handleError(e)
            }
        }

    suspend fun getCeoDog(): Result<CeoDog> =
        withContext(coroutineDispatcherProvider.io()) {
            val response = apiHelper.getDogFromCeo()
            try {
                Result.success(response)
            } catch (e: Exception) {
                handleError(e)
            }
        }

    private fun <AppError> handleError(t: Throwable): Result<AppError> {
        return Result.failure(
            com.example.hanadroid.model.AppError(
                message = t.message,
                cause = t.cause
            )
        )
    }
}

package com.example.hanadroid.usecases

import com.example.hanadroid.model.BoredActivity
import com.example.hanadroid.networking.ResponseWrapper
import com.example.hanadroid.repository.BoredActivityRepository
import com.example.hanadroid.util.CoroutineDispatcherProvider
import kotlinx.coroutines.withContext

class FetchBoredActivityUseCases(
    private val boredActivityRepository: BoredActivityRepository,
    private val coroutineDispatcher: CoroutineDispatcherProvider
) {
    suspend operator fun invoke(type: String): ResponseWrapper<BoredActivity> =
        withContext(coroutineDispatcher.io()) {
            boredActivityRepository.fetchBoredActivityByType(type)
        }

    suspend operator fun invoke(): ResponseWrapper<BoredActivity> =
        withContext(coroutineDispatcher.io()) {
            boredActivityRepository.fetchRandomBoredActivity()
        }

    suspend fun invokeForResult(): Result<BoredActivity> =
        withContext(coroutineDispatcher.io()) {
            boredActivityRepository.fetchRandomBoredActivityForResult()
        }
}

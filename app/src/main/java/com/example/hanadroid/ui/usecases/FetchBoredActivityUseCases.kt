package com.example.hanadroid.ui.usecases

import com.example.hanadroid.data.model.BoredActivity
import com.example.hanadroid.networking.ResponseWrapper
import com.example.hanadroid.ui.repository.BoredActivityRepository
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
}

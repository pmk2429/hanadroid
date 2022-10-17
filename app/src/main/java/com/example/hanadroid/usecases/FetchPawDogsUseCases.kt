package com.example.hanadroid.usecases

import com.example.hanadroid.repository.DogsRepository
import com.example.hanadroid.ui.uistate.DogsUiState
import com.example.hanadroid.util.CoroutineDispatcherProvider
import kotlinx.coroutines.withContext

class FetchPawDogsUseCases(
    private val dogsRepository: DogsRepository,
    private val coroutineDispatcher: CoroutineDispatcherProvider
) {

    suspend fun invoke(): Result<DogsUiState> = withContext(coroutineDispatcher.io()) {
        val woofDog = withContext(coroutineDispatcher.default()) {
            dogsRepository.getWoofDog()
        }
        val ceoDog = withContext(coroutineDispatcher.default()) {
            dogsRepository.getCeoDog()
        }

        if (woofDog.isSuccess && ceoDog.isSuccess) {
            Result.success(
                DogsUiState(
                    woofImageUrl = woofDog.getOrThrow().imageUrl,
                    ceoImageUrl = ceoDog.getOrThrow().message,
                )
            )
        } else {
            Result.failure(woofDog.exceptionOrNull() ?: ceoDog.exceptionOrNull()!!)
        }
    }
}

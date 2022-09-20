package com.example.hanadroid.ui.usecases

import com.example.hanadroid.data.model.University
import com.example.hanadroid.networking.ResponseWrapper
import com.example.hanadroid.ui.repository.UniversityRepository
import com.example.hanadroid.util.CoroutineDispatcherProvider
import kotlinx.coroutines.withContext

class FetchUniversitiesUseCase(
    private val universityRepository: UniversityRepository,
    private val coroutineDispatcher: CoroutineDispatcherProvider
) {
    suspend operator fun invoke(country: String): ResponseWrapper<List<University>> =
        withContext(coroutineDispatcher.io()) {
            universityRepository.fetchUniversities(country)
        }
}
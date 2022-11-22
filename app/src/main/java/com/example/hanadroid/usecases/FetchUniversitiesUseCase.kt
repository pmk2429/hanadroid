package com.example.hanadroid.usecases

import com.example.hanadroid.model.University
import com.example.hanadroid.networking.ResponseWrapper
import com.example.hanadroid.repository.UniversityRepository
import com.example.hanadroid.util.CoroutineDispatcherProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FetchUniversitiesUseCase @Inject constructor(
    private val universityRepository: UniversityRepository,
    private val coroutineDispatcher: CoroutineDispatcherProvider
) {
    suspend operator fun invoke(country: String): ResponseWrapper<List<University>> =
        withContext(coroutineDispatcher.io()) {
            universityRepository.fetchUniversities(country)
        }
}

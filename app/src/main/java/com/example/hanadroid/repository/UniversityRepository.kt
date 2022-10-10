package com.example.hanadroid.repository

import com.example.hanadroid.data.universityapi.UniversityApiHelper
import com.example.hanadroid.model.University
import com.example.hanadroid.networking.ResponseWrapper
import com.example.hanadroid.util.CoroutineDispatcherProvider
import kotlinx.coroutines.withContext

class UniversityRepository(
    private val apiHelper: UniversityApiHelper,
    private val coroutineDispatcher: CoroutineDispatcherProvider
) {
    suspend fun fetchUniversities(
        country: String
    ): ResponseWrapper<List<University>> = withContext(coroutineDispatcher.io()) {
        val result = apiHelper.getUniversitiesByCountry(country)
        try {
            ResponseWrapper.Success(result)
        } catch (e: Exception) {
            ResponseWrapper.Error(e.message!!)
        }
    }
}

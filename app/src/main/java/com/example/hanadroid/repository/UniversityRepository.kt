package com.example.hanadroid.repository

import com.example.hanadroid.data.universityapi.UniversityApiHelper
import com.example.hanadroid.model.University
import com.example.hanadroid.networking.NetworkResult
import com.example.hanadroid.util.CoroutineDispatcherProvider
import javax.inject.Inject

class UniversityRepository @Inject constructor(
    private val apiHelper: UniversityApiHelper,
    coroutineDispatcher: CoroutineDispatcherProvider
) : BaseRepository(coroutineDispatcher) {

    suspend fun fetchUniversities(
        country: String
    ): NetworkResult<List<University>> {
        return safeApiCall { apiHelper.getUniversitiesByCountry(country) }
    }
}

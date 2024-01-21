package com.example.hanadroid.data.universityapi

import com.example.hanadroid.model.University
import retrofit2.Response
import javax.inject.Inject

class UniversityApiHelperImpl @Inject constructor(
    private val universityApiService: UniversityApiService
) : UniversityApiHelper {
    override suspend fun getUniversities(): Response<List<University>> =
        universityApiService.getUniversities()

    override suspend fun getUniversitiesByCountry(country: String): Response<List<University>> =
        universityApiService.getUniversitiesByCountry(country)
}

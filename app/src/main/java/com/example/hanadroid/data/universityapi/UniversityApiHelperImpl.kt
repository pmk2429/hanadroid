package com.example.hanadroid.data.universityapi

import com.example.hanadroid.model.University
import javax.inject.Inject

class UniversityApiHelperImpl @Inject constructor(
    private val universityApiService: UniversityApiService
) : UniversityApiHelper {
    override suspend fun getUniversities(): List<University> =
        universityApiService.getUniversities()

    override suspend fun getUniversitiesByCountry(country: String): List<University> =
        universityApiService.getUniversitiesByCountry(country)
}

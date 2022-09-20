package com.example.hanadroid.data.universityapi

import com.example.hanadroid.data.model.University

class UniversityApiHelperImpl(private val universityApiService: UniversityApiService) : UniversityApiHelper {
    override suspend fun getUniversities(): List<University> =
        universityApiService.getUniversities()

    override suspend fun getUniversitiesByCountry(country: String): List<University> =
        universityApiService.getUniversitiesByCountry(country)
}

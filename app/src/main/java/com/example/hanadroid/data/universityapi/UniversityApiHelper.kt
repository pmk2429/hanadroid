package com.example.hanadroid.data.universityapi

import com.example.hanadroid.model.University
import retrofit2.Response

interface UniversityApiHelper {

    suspend fun getUniversities(): Response<List<University>>

    suspend fun getUniversitiesByCountry(country: String): Response<List<University>>
}
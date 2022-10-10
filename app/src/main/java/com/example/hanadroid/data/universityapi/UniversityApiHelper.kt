package com.example.hanadroid.data.universityapi

import com.example.hanadroid.model.University

interface UniversityApiHelper {

    suspend fun getUniversities(): List<University>

    suspend fun getUniversitiesByCountry(country: String): List<University>
}
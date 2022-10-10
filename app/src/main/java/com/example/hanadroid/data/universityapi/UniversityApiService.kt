package com.example.hanadroid.data.universityapi

import com.example.hanadroid.model.University
import retrofit2.http.GET
import retrofit2.http.Query

interface UniversityApiService {

    @GET("search")
    suspend fun getUniversities(): List<University>

    @GET("search")
    suspend fun getUniversitiesByCountry(@Query("country") country: String): List<University>
}
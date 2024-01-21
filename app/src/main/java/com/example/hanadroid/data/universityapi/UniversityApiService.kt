package com.example.hanadroid.data.universityapi

import com.example.hanadroid.model.University
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UniversityApiService {

    @GET("search")
    suspend fun getUniversities(): Response<List<University>>

    @GET("search")
    suspend fun getUniversitiesByCountry(@Query("country") country: String): Response<List<University>>
}
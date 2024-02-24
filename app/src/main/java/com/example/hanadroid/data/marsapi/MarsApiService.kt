package com.example.hanadroid.data.marsapi

import com.example.hanadroid.model.MarsPhoto
import retrofit2.Response
import retrofit2.http.GET

interface MarsApiService {

    @GET("photos")
    suspend fun getMarsRealEstateData(): Response<List<MarsPhoto>>

}
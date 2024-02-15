package com.example.hanadroid.data.marsapi

import com.example.hanadroid.model.MarsPhoto
import retrofit2.Response

interface MarsApiDataHelper {
    suspend fun getMarsRealEstateData(): Response<List<MarsPhoto>>
}
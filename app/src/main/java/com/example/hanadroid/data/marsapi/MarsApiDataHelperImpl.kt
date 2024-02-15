package com.example.hanadroid.data.marsapi

import com.example.hanadroid.model.MarsPhoto
import retrofit2.Response
import javax.inject.Inject

class MarsApiDataHelperImpl @Inject constructor(
    private val marsApiService: MarsApiService
) : MarsApiDataHelper {
    override suspend fun getMarsRealEstateData(): Response<List<MarsPhoto>> =
        marsApiService.getMarsRealEstateData()
}
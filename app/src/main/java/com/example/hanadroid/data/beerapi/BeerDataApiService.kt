package com.example.hanadroid.data.beerapi

import com.example.hanadroid.model.BeerInfo
import retrofit2.Response
import retrofit2.http.GET

interface BeerDataApiService {

    @GET(".")
    suspend fun getBeerList(): Response<List<BeerInfo>>
}

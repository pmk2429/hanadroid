package com.example.hanadroid.data.beerapi

import com.example.hanadroid.model.BeerInfo
import retrofit2.Response

interface BeerDataApiHelper {
    suspend fun getBeerList(): Response<List<BeerInfo>>
}
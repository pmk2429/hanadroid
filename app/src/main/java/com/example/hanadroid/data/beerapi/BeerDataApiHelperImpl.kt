package com.example.hanadroid.data.beerapi

class BeerDataApiHelperImpl(
    private val apiService: BeerDataApiService
) : BeerDataApiHelper {
    override suspend fun getBeerList() = apiService.getBeerList()

}

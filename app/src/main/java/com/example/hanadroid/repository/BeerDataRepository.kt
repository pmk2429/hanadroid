package com.example.hanadroid.repository

import com.example.hanadroid.data.beerapi.BeerDataApiHelper
import com.example.hanadroid.model.BeerInfo
import com.example.hanadroid.networking.NetworkResult
import com.example.hanadroid.util.CoroutineDispatcherProvider
import javax.inject.Inject

class BeerDataRepository @Inject constructor(
    private val apiHelper: BeerDataApiHelper,
    coroutineDispatcherProvider: CoroutineDispatcherProvider
) : BaseRepository(coroutineDispatcherProvider) {
    suspend fun getBeerDataList(): NetworkResult<List<BeerInfo>> {
        return safeApiCall { apiHelper.getBeerList() }
    }
}

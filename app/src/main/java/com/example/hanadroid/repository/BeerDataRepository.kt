package com.example.hanadroid.repository

import com.example.hanadroid.data.beerapi.BeerDataApiHelper
import com.example.hanadroid.model.BeerInfo
import com.example.hanadroid.networking.ResponseWrapper
import com.example.hanadroid.util.CoroutineDispatcherProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BeerDataRepository @Inject constructor(
    private val apiHelper: BeerDataApiHelper,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider
) {
    suspend fun getBeerDataList(): ResponseWrapper<List<BeerInfo>> =
        withContext(coroutineDispatcherProvider.io()) {
            val beerDataList = apiHelper.getBeerList()
            try {
                ResponseWrapper.Success(beerDataList)
            } catch (e: Exception) {
                ResponseWrapper.Error(e.message!!)
            }
        }
}

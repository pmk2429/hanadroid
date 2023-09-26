package com.example.hanadroid.data.beerapi

import com.example.hanadroid.model.BeerInfo

interface BeerDataApiHelper {
    suspend fun getBeerList(): List<BeerInfo>
}
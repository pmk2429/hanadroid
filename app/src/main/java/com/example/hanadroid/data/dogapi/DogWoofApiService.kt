package com.example.hanadroid.data.dogapi

import com.example.hanadroid.model.WoofDog
import retrofit2.http.GET

interface DogWoofApiService {

    @GET(".")
    suspend fun getWoofDog(): WoofDog

}

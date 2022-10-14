package com.example.hanadroid.data.dogapi

import com.example.hanadroid.model.CeoDog
import retrofit2.http.GET

interface DogCeoApiService {

    @GET(".")
    suspend fun getDogFromCeo(): CeoDog

}

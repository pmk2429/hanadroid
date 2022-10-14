package com.example.hanadroid.data.dogapi

import com.example.hanadroid.model.CeoDog
import com.example.hanadroid.model.WoofDog

class DogApiHelperImpl(
    private val woofDogApiService: DogWoofApiService,
    private val ceoDogApiService: DogCeoApiService
) : DogApiHelper {
    override suspend fun getDogFromWoof(): WoofDog =
        woofDogApiService.getWoofDog()

    override suspend fun getDogFromCeo(): CeoDog =
        ceoDogApiService.getDogFromCeo()
}

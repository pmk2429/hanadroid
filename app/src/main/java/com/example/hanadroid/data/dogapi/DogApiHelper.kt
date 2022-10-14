package com.example.hanadroid.data.dogapi

import com.example.hanadroid.model.CeoDog
import com.example.hanadroid.model.WoofDog

interface DogApiHelper {
    suspend fun getDogFromWoof(): WoofDog

    suspend fun getDogFromCeo(): CeoDog
}

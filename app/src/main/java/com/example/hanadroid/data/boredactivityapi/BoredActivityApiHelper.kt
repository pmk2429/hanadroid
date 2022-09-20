package com.example.hanadroid.data.boredactivityapi

import com.example.hanadroid.data.model.BoredActivity

interface BoredActivityApiHelper {
    suspend fun getRandomBoredActivity(): BoredActivity

    suspend fun getBoredActivityByType(type: String): BoredActivity
}
package com.example.hanadroid.networking

import android.content.Context
import com.example.hanadroid.data.boredactivityapi.BoredActivityApiService
import com.example.hanadroid.data.universityapi.UniversityApiService
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

object RetrofitBuilder {

    private const val UNIVERSITY_BASE_URL: String = "http://universities.hipolabs.com/"
    private const val BORED_ACTIVITY_BASE_URL: String = "http://www.boredapi.com/api/activity/"

    private fun createUniversity(): Retrofit {
        val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

        // val cacheDir = context?.cacheDir
        // val cacheSize = 10L * 1024L * 1024L // 10 MB
        // .cache(Cache(File(""), cacheSize))

        val client = OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()

        return Retrofit.Builder()
            .baseUrl(UNIVERSITY_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    private fun createBoredActivityRetrofit(): Retrofit {
        val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

        val client = OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()

        return Retrofit.Builder()
            .baseUrl(BORED_ACTIVITY_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val universityApiService: UniversityApiService by lazy {
        createUniversity().create(UniversityApiService::class.java)
    }

    val boredActivityApiService: BoredActivityApiService by lazy {
        createBoredActivityRetrofit().create(BoredActivityApiService::class.java)
    }

}

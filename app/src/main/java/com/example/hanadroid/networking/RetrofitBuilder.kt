package com.example.hanadroid.networking

import com.example.hanadroid.data.boredactivityapi.BoredActivityApiService
import com.example.hanadroid.data.rickandmorty.RickAndMortyApiService
import com.example.hanadroid.data.universityapi.UniversityApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {

    private fun createRetrofitService(baseUrl: String): Retrofit {
        val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

        // val cacheDir = context?.cacheDir
        // val cacheSize = 10L * 1024L * 1024L // 10 MB
        // .cache(Cache(File(""), cacheSize))

        val client = OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    private fun createUniversityRetrofit(): Retrofit =
        createRetrofitService(UNIVERSITY_BASE_URL)

    private fun createBoredActivityRetrofit(): Retrofit =
        createRetrofitService(BORED_ACTIVITY_BASE_URL)

    private fun createWoofRetrofit(): Retrofit = createRetrofitService(WOOF_DOG_BASE_URL)
    private fun createDogCEORetrofit(): Retrofit = createRetrofitService(DOG_CEO_BASE_URL)

    private fun createRickAndMortyRetrofit(): Retrofit =
        createRetrofitService(RICK_AND_MORTY_BASE_URL)

    val universityApiService: UniversityApiService by lazy {
        createUniversityRetrofit().create(UniversityApiService::class.java)
    }

    val boredActivityApiService: BoredActivityApiService by lazy {
        createBoredActivityRetrofit().create(BoredActivityApiService::class.java)
    }

    val rickAndMortyApiService: RickAndMortyApiService by lazy {
        createRickAndMortyRetrofit().create(RickAndMortyApiService::class.java)
    }
}

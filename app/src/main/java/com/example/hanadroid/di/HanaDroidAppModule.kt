package com.example.hanadroid.di

import com.example.hanadroid.data.beerapi.BeerDataApiHelper
import com.example.hanadroid.data.beerapi.BeerDataApiHelperImpl
import com.example.hanadroid.data.beerapi.BeerDataApiService
import com.example.hanadroid.data.boredactivityapi.BoredActivityApiService
import com.example.hanadroid.data.disney.DisneyCharactersApiHelper
import com.example.hanadroid.data.disney.DisneyCharactersApiHelperImpl
import com.example.hanadroid.data.disney.DisneyCharactersApiService
import com.example.hanadroid.data.dogapi.DogCeoApiService
import com.example.hanadroid.data.dogapi.DogWoofApiService
import com.example.hanadroid.data.rickandmorty.RickAndMortyApiService
import com.example.hanadroid.data.universityapi.UniversityApiHelper
import com.example.hanadroid.data.universityapi.UniversityApiHelperImpl
import com.example.hanadroid.data.universityapi.UniversityApiService
import com.example.hanadroid.networking.BEER_INFO_BASE_URL
import com.example.hanadroid.networking.BORED_ACTIVITY_BASE_URL
import com.example.hanadroid.networking.CEO_DOG_BASE_URL
import com.example.hanadroid.networking.DISNEY_BASE_URL
import com.example.hanadroid.networking.RICK_AND_MORTY_BASE_URL
import com.example.hanadroid.networking.UNIVERSITY_BASE_URL
import com.example.hanadroid.networking.WOOF_DOG_BASE_URL
import com.example.hanadroid.repository.UniversityRepository
import com.example.hanadroid.util.CoroutineDispatcherProvider
import com.example.hanadroid.util.DefaultDispatcherProvider
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class HanaDroidAppModule {

    private fun createRetrofitService(baseUrl: String): Retrofit {
        val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

        val client = OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    private fun createRetrofitServiceWithMoshi(baseUrl: String): Retrofit {
        val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

        val client = OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
    }

    @Singleton
    @Provides
    fun providesUniversityApiService(): UniversityApiService {
        return createRetrofitService(UNIVERSITY_BASE_URL).create(UniversityApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideUniversityApiHelper(): UniversityApiHelper {
        return UniversityApiHelperImpl(providesUniversityApiService())
    }

    @Provides
    @Singleton
    fun provideUniversityRepository(
        apiHelper: UniversityApiHelper,
        coroutineDispatcher: CoroutineDispatcherProvider
    ) = UniversityRepository(apiHelper, coroutineDispatcher)

    @Singleton
    @Provides
    fun providesBoredActivityApiService(): BoredActivityApiService {
        return createRetrofitService(BORED_ACTIVITY_BASE_URL).create(BoredActivityApiService::class.java)
    }

    @Singleton
    @Provides
    fun providesRickAndMortyApiService(): RickAndMortyApiService {
        return createRetrofitService(RICK_AND_MORTY_BASE_URL).create(RickAndMortyApiService::class.java)
    }

    @Singleton
    @Provides
    fun providesDogWoofApiService(): DogWoofApiService {
        return createRetrofitService(WOOF_DOG_BASE_URL).create(DogWoofApiService::class.java)
    }

    @Singleton
    @Provides
    fun providesDogCeoApiService(): DogCeoApiService {
        return createRetrofitService(CEO_DOG_BASE_URL).create(DogCeoApiService::class.java)
    }

    @Singleton
    @Provides
    fun providesDisneyCharactersApiService(): DisneyCharactersApiService {
        return createRetrofitService(DISNEY_BASE_URL).create(DisneyCharactersApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideDisneyApiHelper(): DisneyCharactersApiHelper {
        return DisneyCharactersApiHelperImpl(providesDisneyCharactersApiService())
    }

    @Singleton
    @Provides
    fun providesBeerDataApiService(): BeerDataApiService {
        return createRetrofitService(BEER_INFO_BASE_URL).create(BeerDataApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideBeerDataApiHelper(): BeerDataApiHelper {
        return BeerDataApiHelperImpl(providesBeerDataApiService())
    }

    @Singleton
    @Provides
    fun provideCoroutineDispatcher(): CoroutineDispatcherProvider {
        return DefaultDispatcherProvider()
    }

}

package com.example.hanadroid.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.hanadroid.data.beerapi.BeerDataApiHelper
import com.example.hanadroid.data.beerapi.BeerDataApiHelperImpl
import com.example.hanadroid.data.beerapi.BeerDataApiService
import com.example.hanadroid.data.boredactivityapi.BoredActivityApiService
import com.example.hanadroid.data.disneyapi.DisneyApiHelper
import com.example.hanadroid.data.disneyapi.DisneyApiHelperImpl
import com.example.hanadroid.data.disneyapi.DisneyApiService
import com.example.hanadroid.data.dogapi.DogCeoApiService
import com.example.hanadroid.data.dogapi.DogWoofApiService
import com.example.hanadroid.data.marsapi.MarsApiDataHelper
import com.example.hanadroid.data.marsapi.MarsApiDataHelperImpl
import com.example.hanadroid.data.marsapi.MarsApiService
import com.example.hanadroid.data.nextdoor.NewsApiHelperImpl
import com.example.hanadroid.data.nextdoor.NewsFeedApiHelper
import com.example.hanadroid.data.nextdoor.NewsFeedApiService
import com.example.hanadroid.data.rickandmorty.RickAndMortyApiService
import com.example.hanadroid.data.universityapi.UniversityApiHelper
import com.example.hanadroid.data.universityapi.UniversityApiHelperImpl
import com.example.hanadroid.data.universityapi.UniversityApiService
import com.example.hanadroid.networking.BEER_INFO_BASE_URL
import com.example.hanadroid.networking.BORED_ACTIVITY_BASE_URL
import com.example.hanadroid.networking.CEO_DOG_BASE_URL
import com.example.hanadroid.networking.DISNEY_BASE_URL
import com.example.hanadroid.networking.MARS_REAL_ESTATE_URL
import com.example.hanadroid.networking.NEWS_FEED_BASE_URL
import com.example.hanadroid.networking.RICK_AND_MORTY_BASE_URL
import com.example.hanadroid.networking.RetryInterceptor
import com.example.hanadroid.networking.UNIVERSITY_BASE_URL
import com.example.hanadroid.networking.WOOF_DOG_BASE_URL
import com.example.hanadroid.repository.UniversityRepository
import com.example.hanadroid.repository.UserPreferencesRepository
import com.example.hanadroid.util.CoroutineDispatcherProvider
import com.example.hanadroid.util.DefaultDispatcherProvider
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

private const val HANA_PREFERENCES_NAME = "hana_preferences"

@Module
@InstallIn(SingletonComponent::class)
class HanaDroidAppModule {

    private fun createRetrofitService(baseUrl: String): Retrofit {
        val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

        val client = OkHttpClient.Builder()
            .addInterceptor(logger)
            //.addInterceptor(ChuckerInterceptor(context))
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
            .addInterceptor(RetryInterceptor(maxRetries = 3)) // Retry up to 3 times
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
    fun provideMarsDataApiService(): MarsApiService {
        return createRetrofitServiceWithMoshi(MARS_REAL_ESTATE_URL).create(MarsApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideMarsDataApiHelper(): MarsApiDataHelper {
        return MarsApiDataHelperImpl(provideMarsDataApiService())
    }

    @Provides
    @Singleton
    fun provideNewsFeedApiService(): NewsFeedApiService {
        return createRetrofitServiceWithMoshi(NEWS_FEED_BASE_URL).create(NewsFeedApiService::class.java)
    }

    @Provides
    @Singleton
    fun providesNewsFeedApiHelper(): NewsFeedApiHelper {
        return NewsApiHelperImpl(provideNewsFeedApiService())
    }

    @Provides
    @Singleton
    fun provideDisneyApiService(): DisneyApiService {
        return createRetrofitServiceWithMoshi(DISNEY_BASE_URL).create(DisneyApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideDisneyApiHelper(): DisneyApiHelper {
        return DisneyApiHelperImpl(provideDisneyApiService())
    }

    @Singleton
    @Provides
    fun provideCoroutineDispatcher(): CoroutineDispatcherProvider {
        return DefaultDispatcherProvider()
    }

    @Singleton
    @Provides
    fun providePreferencesDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            migrations = listOf(SharedPreferencesMigration(context, HANA_PREFERENCES_NAME)),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { context.preferencesDataStoreFile(HANA_PREFERENCES_NAME) }
        )
    }

    @Provides
    @Singleton
    fun provideUserPreferencesRepository(dataStore: DataStore<Preferences>): UserPreferencesRepository {
        return UserPreferencesRepository(dataStore)
    }

}

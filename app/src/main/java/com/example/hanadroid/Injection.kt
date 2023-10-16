package com.example.hanadroid

import android.content.Context
import com.example.hanadroid.data.boredactivityapi.BoredActivityApiHelperImpl
import com.example.hanadroid.data.dogapi.DogApiHelperImpl
import com.example.hanadroid.data.universityapi.UniversityApiHelperImpl
import com.example.hanadroid.db.RickMortyDatabase
import com.example.hanadroid.networking.RetrofitBuilder
import com.example.hanadroid.repository.ArticleRepository
import com.example.hanadroid.repository.BoredActivityRepository
import com.example.hanadroid.repository.DogsRepository
import com.example.hanadroid.repository.RickAndMortyRepository
import com.example.hanadroid.repository.UniversityRepository
import com.example.hanadroid.usecases.FetchBoredActivityUseCases
import com.example.hanadroid.usecases.FetchPawDogsUseCases
import com.example.hanadroid.util.DefaultDispatcherProvider

object Injection {

    fun createBoredActivityUseCase(): FetchBoredActivityUseCases =
        FetchBoredActivityUseCases(
            createBoredActivityRepository(),
            DefaultDispatcherProvider()
        )

    fun createDogsUseCase(): FetchPawDogsUseCases =
        FetchPawDogsUseCases(
            createDogsRepository(),
            DefaultDispatcherProvider()
        )

    private fun createDogsRepository(): DogsRepository =
        DogsRepository(
            DogApiHelperImpl(RetrofitBuilder.woofDogApiService, RetrofitBuilder.ceoDogApiService),
            DefaultDispatcherProvider()
        )

    fun provideArticleRepository(): ArticleRepository = ArticleRepository()

    fun provideRickAndMortyCharactersRepository(context: Context): RickAndMortyRepository =
        RickAndMortyRepository(
            RetrofitBuilder.rickAndMortyApiService,
            RickMortyDatabase.getInstance(context)
        )

    fun createUniversityRepository(): UniversityRepository =
        UniversityRepository(
            UniversityApiHelperImpl(RetrofitBuilder.universityApiService),
            DefaultDispatcherProvider()
        )

    private fun createBoredActivityRepository(): BoredActivityRepository =
        BoredActivityRepository(
            BoredActivityApiHelperImpl(RetrofitBuilder.boredActivityApiService),
            DefaultDispatcherProvider()
        )
}

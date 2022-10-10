package com.example.hanadroid

import android.content.Context
import com.example.hanadroid.data.boredactivityapi.BoredActivityApiHelperImpl
import com.example.hanadroid.data.universityapi.UniversityApiHelperImpl
import com.example.hanadroid.db.RedditDatabase
import com.example.hanadroid.networking.RetrofitBuilder
import com.example.hanadroid.repository.ArticleRepository
import com.example.hanadroid.repository.BoredActivityRepository
import com.example.hanadroid.repository.RedditRepository
import com.example.hanadroid.repository.UniversityRepository
import com.example.hanadroid.usecases.FetchBoredActivityUseCases
import com.example.hanadroid.usecases.FetchUniversitiesUseCase
import com.example.hanadroid.util.DefaultDispatcherProvider

object Injection {

    fun createFetchUniversitiesUseCase(): FetchUniversitiesUseCase {
        return FetchUniversitiesUseCase(createUniversityRepository(), DefaultDispatcherProvider)
    }

    fun createBoredActivityUseCase(): FetchBoredActivityUseCases {
        return FetchBoredActivityUseCases(
            createBoredActivityRepository(),
            DefaultDispatcherProvider
        )
    }

    fun provideArticleRepository(): ArticleRepository = ArticleRepository()

    fun provideRedditPostRepository(context: Context): RedditRepository {
        return RedditRepository(
            RetrofitBuilder.redditPostApiService,
            RedditDatabase.create(context = context)
        )
    }

    private fun createUniversityRepository(): UniversityRepository {
        return UniversityRepository(
            UniversityApiHelperImpl(RetrofitBuilder.universityApiService),
            DefaultDispatcherProvider
        )
    }

    private fun createBoredActivityRepository(): BoredActivityRepository {
        return BoredActivityRepository(
            BoredActivityApiHelperImpl(RetrofitBuilder.boredActivityApiService),
            DefaultDispatcherProvider
        )
    }

}

package com.example.hanadroid.di

import com.example.hanadroid.data.universityapi.UniversityApiHelper
import com.example.hanadroid.repository.UniversityRepository
import com.example.hanadroid.util.CoroutineDispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class HanaDroidAppModule {

    @Provides
    @Singleton
    fun provideRepository(
        apiHelper: UniversityApiHelper,
        coroutineDispatcher: CoroutineDispatcherProvider
    ) = UniversityRepository(apiHelper, coroutineDispatcher)

}

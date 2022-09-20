package com.example.hanadroid.ui.viewmodels

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.example.hanadroid.data.boredactivityapi.BoredActivityApiHelperImpl
import com.example.hanadroid.data.universityapi.UniversityApiHelperImpl
import com.example.hanadroid.networking.RetrofitBuilder
import com.example.hanadroid.ui.repository.BoredActivityRepository
import com.example.hanadroid.ui.repository.UniversityRepository
import com.example.hanadroid.ui.usecases.FetchBoredActivityUseCases
import com.example.hanadroid.ui.usecases.FetchUniversitiesUseCase
import com.example.hanadroid.util.DefaultDispatcherProvider

@Suppress("UNCHECKED_CAST")
class HanaViewModelFactory(
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle? = null,
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {

    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        if (modelClass.isAssignableFrom(UniversityListViewModel::class.java)) {
            return UniversityListViewModel(handle, createFetchUniversitiesUseCase()) as T
        }
        if (modelClass.isAssignableFrom(BoredActivityViewModel::class.java)) {
            return BoredActivityViewModel(handle, createBoredActivityUseCase()) as T
        }
        throw IllegalArgumentException("Unknown Class Identifier")
    }

    private fun createFetchUniversitiesUseCase(): FetchUniversitiesUseCase {
        return FetchUniversitiesUseCase(createUniversityRepository(), DefaultDispatcherProvider)
    }

    private fun createUniversityRepository(): UniversityRepository {
        return UniversityRepository(
            UniversityApiHelperImpl(RetrofitBuilder.universityApiService),
            DefaultDispatcherProvider
        )
    }

    private fun createBoredActivityUseCase(): FetchBoredActivityUseCases {
        return FetchBoredActivityUseCases(
            createBoredActivityRepository(),
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
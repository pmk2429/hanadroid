package com.example.hanadroid.viewmodels

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.example.hanadroid.Injection

@Suppress("UNCHECKED_CAST")
class HanaViewModelFactory(
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle? = null,
    private val context: Context? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {

    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        if (modelClass.isAssignableFrom(UniversityListViewModel::class.java)) {
            return UniversityListViewModel(handle, Injection.createFetchUniversitiesUseCase()) as T
        }
        if (modelClass.isAssignableFrom(BoredActivityViewModel::class.java)) {
            return BoredActivityViewModel(handle, Injection.createBoredActivityUseCase()) as T
        }
        if (modelClass.isAssignableFrom(ArticleViewModel::class.java)) {
            return ArticleViewModel(Injection.provideArticleRepository()) as T
        }
        if (modelClass.isAssignableFrom(RickAndMortyViewModel::class.java)) {
            return RickAndMortyViewModel(
                Injection.provideRickAndMortyCharactersRepository(context!!)
            ) as T
        }
        throw IllegalArgumentException("Unknown Class Identifier")
    }
}
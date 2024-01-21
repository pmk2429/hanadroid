package com.example.hanadroid.viewmodels

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.example.hanadroid.di.Injection
import com.example.hanadroid.repository.NonHiltItemRepository

@Suppress("UNCHECKED_CAST")
class HanaViewModelFactory(
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle? = null,
    private val context: Context? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {

    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        if (modelClass.isAssignableFrom(BoredActivityViewModel::class.java)) {
            return BoredActivityViewModel(Injection.createBoredActivityUseCase()) as T
        }
        if (modelClass.isAssignableFrom(RickAndMortyViewModel::class.java)) {
            return RickAndMortyViewModel(
                Injection.provideRickAndMortyCharactersRepository(context!!)
            ) as T
        }
        if (modelClass.isAssignableFrom(DogsViewModel::class.java)) {
            return DogsViewModel(handle, Injection.createDogsUseCase()) as T
        }
        if (modelClass.isAssignableFrom(NonHiltViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NonHiltViewModel(NonHiltItemRepository()) as T
        }
        throw IllegalArgumentException("Unknown Class Identifier")
    }
}

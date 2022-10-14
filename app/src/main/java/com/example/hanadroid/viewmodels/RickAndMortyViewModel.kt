package com.example.hanadroid.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.hanadroid.model.RickMortyCharacter
import com.example.hanadroid.repository.RickAndMortyRepository
import kotlinx.coroutines.flow.Flow

class RickAndMortyViewModel(
    private val rickAndMortyRepository: RickAndMortyRepository
) : ViewModel() {

    private lateinit var _charactersFlow: Flow<PagingData<RickMortyCharacter>>
    val charactersFlow: Flow<PagingData<RickMortyCharacter>>
        get() = _charactersFlow

    init {
        getAllCharacters()
    }

    private fun getAllCharacters() {
        _charactersFlow = rickAndMortyRepository
            .getAllCharacters()
            .cachedIn(viewModelScope)
    }
}

/*
    Tip for Android apps! You can use WhileSubscribed(5000) most of the time to keep the upstream
    flow active for 5 seconds more after the disappearance of the last collector.
    That avoids restarting the upstream flow in certain situations such as configuration changes.
    This tip is especially helpful when upstream flows are expensive to create and when these
    operators are used in ViewModels.
 */

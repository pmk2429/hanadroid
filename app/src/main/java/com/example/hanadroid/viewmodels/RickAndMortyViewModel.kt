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

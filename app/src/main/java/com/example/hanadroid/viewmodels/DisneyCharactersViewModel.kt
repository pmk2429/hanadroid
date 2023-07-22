package com.example.hanadroid.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hanadroid.networking.ResponseWrapper
import com.example.hanadroid.repository.DisneyCharactersRepository
import com.example.hanadroid.ui.uistate.DisneyCharactersUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DisneyCharactersViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val disneyCharactersRepository: DisneyCharactersRepository
) : ViewModel() {

    private var getDisneyCharactersJob: Job? = null

    private var _disneyCharactersUiState =
        MutableStateFlow(DisneyCharactersUiState(isLoading = true))
    val disneyCharactersUiState get() = _disneyCharactersUiState

    init {
        fetchDisneyCharacters()
    }

    private fun fetchDisneyCharacters() {
        if (getDisneyCharactersJob?.isActive == true) {
            return
        }

        getDisneyCharactersJob = viewModelScope.launch {
            when (val result = disneyCharactersRepository.fetchDisneyCharacters(2, 50)) {
                is ResponseWrapper.Success -> {
                    _disneyCharactersUiState.update { currentUiState ->
                        currentUiState.copy(characters = result.data)
                    }
                }
                is ResponseWrapper.Error -> {
                    _disneyCharactersUiState.update { currentUiState ->
                        currentUiState.copy(failureMessage = result.failureMessage)
                    }
                }
            }
        }
    }
}

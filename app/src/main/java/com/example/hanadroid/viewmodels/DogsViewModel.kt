package com.example.hanadroid.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hanadroid.ui.uistate.DogsUiState
import com.example.hanadroid.usecases.FetchPawDogsUseCases
import com.example.hanadroid.util.toAppError
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DogsViewModel(
    savedStateHandle: SavedStateHandle,
    private val dogsUseCases: FetchPawDogsUseCases
) : ViewModel() {

    private var getDogsActivityJob: Job? = null

    private var _dogsUiState: MutableStateFlow<DogsUiState> =
        MutableStateFlow(DogsUiState(isLoading = true))
    val dogsUiState: StateFlow<DogsUiState> = _dogsUiState

    init {
        fetchFurryFriends()
    }

    private fun fetchFurryFriends() {
        if (getDogsActivityJob?.isActive == true) {
            return
        }

        getDogsActivityJob = viewModelScope.launch {
            val response = dogsUseCases.invoke()
            if (response.isSuccess) {
                response.getOrThrow().apply {
                    _dogsUiState.update { currentUiState ->
                        currentUiState.copy(
                            woofImageUrl = this.woofImageUrl,
                            ceoImageUrl = this.ceoImageUrl,
                            isLoading = false
                        )
                    }
                }
            } else {
                _dogsUiState.update { currentUiState ->
                    currentUiState.copy(
                        failureMessage = response.exceptionOrNull().toAppError().message
                    )
                }
            }
        }
    }

    fun fetchRandomCuteDogsMedia() {
        fetchFurryFriends()
    }

}

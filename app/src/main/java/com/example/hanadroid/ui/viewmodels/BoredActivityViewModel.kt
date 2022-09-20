package com.example.hanadroid.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hanadroid.networking.ResponseWrapper
import com.example.hanadroid.ui.uistate.BoredActivityUiState
import com.example.hanadroid.ui.usecases.FetchBoredActivityUseCases
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BoredActivityViewModel(
    savedStateHandle: SavedStateHandle,
    private val boredActivityUseCases: FetchBoredActivityUseCases
) : ViewModel() {

    private var getBoredActivityJob: Job? = null

    private var _boredActivityUiState: MutableStateFlow<BoredActivityUiState> =
        MutableStateFlow((BoredActivityUiState(isLoading = true)))
    val boredActivityUiState: StateFlow<BoredActivityUiState> get() = _boredActivityUiState

    fun fetchRandomBoredActivity() {
        if (getBoredActivityJob?.isActive == true) {
            return
        }

        getBoredActivityJob = viewModelScope.launch {
            when (val result = boredActivityUseCases.invoke()) {
                is ResponseWrapper.Success -> {
                    _boredActivityUiState.update { currentUiState ->
                        currentUiState.copy(
                            name = result.data.activity,
                            type = result.data.type,
                            participants = result.data.participants,
                            price = result.data.price,
                            url = result.data.link,
                            isLoading = false
                        )
                    }
                }
                is ResponseWrapper.Error -> {
                    _boredActivityUiState.update { currentUiState ->
                        currentUiState.copy(failureMessage = result.failureMessage)
                    }
                }
            }
        }
    }

}

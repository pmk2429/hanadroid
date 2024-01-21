package com.example.hanadroid.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hanadroid.networking.NetworkResult
import com.example.hanadroid.ui.uistate.BoredActivityUiState
import com.example.hanadroid.usecases.FetchBoredActivityUseCases
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BoredActivityViewModel(
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
                is NetworkResult.Success -> {
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

                is NetworkResult.Error -> {
                    _boredActivityUiState.update { currentUiState ->
                        currentUiState.copy(failureMessage = result.message)
                    }
                }

                is NetworkResult.Exception -> {
                    _boredActivityUiState.update { currentUiState ->
                        currentUiState.copy(failureMessage = result.e.message)
                    }
                }
            }
        }
    }

    /**
     * Emits Int values every seconds until 60 times.
     */
    fun triggerFlow(): Flow<Int> {
        return flow {
            repeat(60) {
                emit(it + 1)
                delay(1000L)
            }
        }
    }

}

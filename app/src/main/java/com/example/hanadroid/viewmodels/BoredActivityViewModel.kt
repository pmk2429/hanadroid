package com.example.hanadroid.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hanadroid.networking.ResponseWrapper
import com.example.hanadroid.ui.uistate.BoredActivityUiState
import com.example.hanadroid.usecases.FetchBoredActivityUseCases
import com.example.hanadroid.util.toAppError
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
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

    fun fetchRandomBoredActivityForResult() {
        if (getBoredActivityJob?.isActive == true) {
            return
        }

        getBoredActivityJob = viewModelScope.launch {
            val boredActivityResult = boredActivityUseCases.invokeForResult()
            if (boredActivityResult.isSuccess) {
                boredActivityResult.getOrThrow().apply {
                    _boredActivityUiState.update { currentUiState ->
                        currentUiState.copy(
                            name = activity,
                            type = type,
                            participants = participants,
                            price = price,
                            url = link,
                            isLoading = false
                        )
                    }
                }
            } else {
                _boredActivityUiState.update { currentUiState ->
                    currentUiState.copy(
                        failureMessage = boredActivityResult.exceptionOrNull().toAppError().message
                    )
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

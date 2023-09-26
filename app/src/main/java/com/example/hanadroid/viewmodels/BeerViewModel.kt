package com.example.hanadroid.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hanadroid.networking.ResponseWrapper
import com.example.hanadroid.repository.BeerDataRepository
import com.example.hanadroid.ui.uistate.BeerDataUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BeerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val beerDataRepository: BeerDataRepository
) : ViewModel() {

    private var getBeerDataJob: Job? = null

    private var _beerDataUiStateFlow =
        MutableStateFlow(BeerDataUiState(isLoading = true))
    val beerDataUiState: StateFlow<BeerDataUiState> get() = _beerDataUiStateFlow

    init {
        fetchBeerData()
    }

    private fun fetchBeerData() {
        if (getBeerDataJob?.isActive == true) {
            return
        }

        // fetch Crypto Data
        getBeerDataJob = viewModelScope.launch {
            val result = beerDataRepository.getBeerDataList()
            when (result) {
                is ResponseWrapper.Success -> {
                    _beerDataUiStateFlow.update { currentUiState ->
                        currentUiState.copy(
                            isLoading = false,
                            beerList = result.data
                        )
                    }
                }

                is ResponseWrapper.Error -> {
                    _beerDataUiStateFlow.update { currentUiState ->
                        currentUiState.copy(
                            isLoading = false,
                            failureMessage = result.failureMessage
                        )
                    }
                }
            }
        }
    }
}

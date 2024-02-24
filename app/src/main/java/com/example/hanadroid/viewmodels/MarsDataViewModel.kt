package com.example.hanadroid.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hanadroid.networking.NetworkResult
import com.example.hanadroid.repository.MarsRealEstateDataRepository
import com.example.hanadroid.ui.uistate.MarsDataUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarsDataViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val marsDataRepository: MarsRealEstateDataRepository
) : ViewModel() {

    private var marsDataFetchJob: Job? = null

    private var _marsDataUiState = MutableStateFlow(MarsDataUiState(isLoading = true))
    val marsDataUiState: StateFlow<MarsDataUiState> get() = _marsDataUiState

    init {
        fetchMarsRealEstateData()
    }

    private fun fetchMarsRealEstateData() {
        if (marsDataFetchJob?.isActive == true) {
            return
        }

        marsDataFetchJob = viewModelScope.launch {
            val response = marsDataRepository.getMarsRealEstateData()
            when (response) {
                is NetworkResult.Success -> {
                    _marsDataUiState.update { currUiState ->
                        currUiState.copy(
                            marsDataList = response.data.ifEmpty { emptyList() },
                            isLoading = false
                        )
                    }
                }

                is NetworkResult.Error -> {
                    _marsDataUiState.update { currUiState ->
                        currUiState.copy(
                            failureMessage = response.message,
                            isLoading = false
                        )
                    }
                }

                is NetworkResult.Exception -> {
                    _marsDataUiState.update { currUiState ->
                        currUiState.copy(
                            failureMessage = response.e.message,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun cancelMarsDataFetch() {
        marsDataFetchJob?.let {
            if (it.isActive) {
                it.cancel()
            }
        }
    }

}
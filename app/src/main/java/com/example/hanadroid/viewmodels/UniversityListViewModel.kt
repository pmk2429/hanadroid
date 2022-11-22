package com.example.hanadroid.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hanadroid.model.University
import com.example.hanadroid.networking.ResponseWrapper
import com.example.hanadroid.ui.uistate.UniversityListUiState
import com.example.hanadroid.usecases.FetchUniversitiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UniversityListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val fetchUniversitiesUseCase: FetchUniversitiesUseCase
) : ViewModel() {

    private var getUniversitiesJob: Job? = null

    private val country = savedStateHandle.get<String>("ARGS_KEY_COUNTRY") ?: "United States"

    private var _universityUiState = MutableStateFlow(UniversityListUiState(isLoading = true))
    val universityUiState: StateFlow<UniversityListUiState>
        get() = _universityUiState

    init {
        fetchUniversitiesByCountry()
    }

    private fun fetchUniversitiesByCountry() {
        if (getUniversitiesJob?.isActive == true) {
            // Already fetching Universities
            return
        }

        getUniversitiesJob = viewModelScope.launch {
            when (val result = fetchUniversitiesUseCase.invoke(country)) {
                is ResponseWrapper.Success -> {
                    _universityUiState.update { currentUiState ->
                        currentUiState.copy(universities = result.data)
                    }
                }
                is ResponseWrapper.Error -> {
                    _universityUiState.update { currentUiState ->
                        currentUiState.copy(failureMessage = result.failureMessage)
                    }
                }
            }
        }
    }
}

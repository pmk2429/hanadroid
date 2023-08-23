package com.example.hanadroid.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hanadroid.networking.ResponseWrapper
import com.example.hanadroid.repository.UniversityRepository
import com.example.hanadroid.ui.fragments.UniversityListFragment.Companion.ARGS_UNIVERSITY_COUNTRY_KEY
import com.example.hanadroid.ui.uistate.UniversityListUiState
import com.example.hanadroid.util.CountryList
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
    private val universityRepository: UniversityRepository,
) : ViewModel() {

    private var getUniversitiesJob: Job? = null

    private var country =
        savedStateHandle.get<String>(ARGS_UNIVERSITY_COUNTRY_KEY) ?: CountryList.unitedStates

    private var _universityUiState = MutableStateFlow(UniversityListUiState(isLoading = true))
    val universityUiState: StateFlow<UniversityListUiState>
        get() = _universityUiState

    init {
        fetchUniversitiesByCountry()
    }

    fun fetchUniversitiesByCountry() {
        if (getUniversitiesJob?.isActive == true) {
            // Already fetching Universities
            return
        }

        country = CountryList.randomCountry

        getUniversitiesJob = viewModelScope.launch {
            when (val result = universityRepository.fetchUniversities(country)) {
                is ResponseWrapper.Success -> {
                    _universityUiState.update { currentUiState ->
                        currentUiState.copy(universities = result.data, isLoading = false)
                    }
                }

                is ResponseWrapper.Error -> {
                    _universityUiState.update { currentUiState ->
                        currentUiState.copy(
                            failureMessage = result.failureMessage,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun cancelUniversitiesFetch() {
        getUniversitiesJob?.let {
            if (it.isActive) {
                it.cancel()
            }
        }
    }
}

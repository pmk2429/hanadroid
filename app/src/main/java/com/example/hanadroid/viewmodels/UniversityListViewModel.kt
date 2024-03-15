package com.example.hanadroid.viewmodels

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.hanadroid.model.University
import com.example.hanadroid.networking.NetworkResult
import com.example.hanadroid.repository.SortOrder
import com.example.hanadroid.repository.UniversityRepository
import com.example.hanadroid.repository.UserPreferencesRepository
import com.example.hanadroid.ui.fragments.UniversityListFragment.Companion.ARGS_UNIVERSITY_COUNTRY_KEY
import com.example.hanadroid.ui.uistate.UniversityListUiState
import com.example.hanadroid.util.CountryList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UniversityListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val universityRepository: UniversityRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private var getUniversitiesJob: Job? = null

    private var country =
        savedStateHandle.get<String>(ARGS_UNIVERSITY_COUNTRY_KEY) ?: CountryList.unitedStates

    private var _universityUiState =
        MutableStateFlow<UniversityListUiState>(UniversityListUiState.LoadingState)
    val universityUiState: StateFlow<UniversityListUiState>
        get() = _universityUiState

    private var _localUniversities = listOf<University>()

    // Keep the user preferences as a stream of changes
    private val userPreferencesFlow = userPreferencesRepository.userPreferencesFlow

    // Initial setup to fetch user preferences when rendering layout
    val initialSetupEvent = liveData {
        emit(userPreferencesRepository.fetchInitialPreferences())
    }

    init {
        fetchUniversitiesByCountry()
    }

    private fun fetchUniversitiesByCountry() {
        if (getUniversitiesJob?.isActive == true) {
            // Already fetching Universities
            return
        }

        country = CountryList.randomCountry

        getUniversitiesJob = viewModelScope.launch {
            when (val result = universityRepository.fetchUniversities(country)) {
                is NetworkResult.Success -> {
                    _localUniversities = result.data
                    if (result.data.isEmpty()) {
                        UniversityListUiState.EmptyListState
                    } else {
                        _universityUiState.update { _ ->
                            UniversityListUiState.ListState(
                                universitiesList = result.data,
                                sortOrder = SortOrder.NONE
                            )
                        }
                    }
                }

                is NetworkResult.Error -> {
                    _universityUiState.update { _ ->
                        UniversityListUiState.ErrorState(failureMessage = result.message)
                    }
                }

                is NetworkResult.Exception -> {
                    _universityUiState.update { _ ->
                        UniversityListUiState.ErrorState(failureMessage = result.e.message)
                    }
                }
            }
        }
    }

    fun refreshUniversitiesFetch() {
        _universityUiState.update {
            UniversityListUiState.LoadingState
        }
        fetchUniversitiesByCountry()
    }

    fun cancelUniversitiesFetch() {
        getUniversitiesJob?.let {
            if (it.isActive) {
                it.cancel()
            }
        }
    }

    /**
     * Combine is for grouping the results of multiple flows into a single object.
     * To simply create a new Flow that passes through the individual emissions from each
     * one independently, merge is the correct operator to use here
     */
    private fun fetchUniversitiesDataSorted() {
        if (getUniversitiesJob?.isActive == true) {
            return
        }

        val exceptionHandler = CoroutineExceptionHandler { _, _ ->
            // Handle the exception here
        }

        getUniversitiesJob = viewModelScope.launch(exceptionHandler) {
            val result = universityRepository.fetchUniversities(country)
            when (result) {
                is NetworkResult.Success -> {
                    val universitiesResponse = result.data
                    if (universitiesResponse.isEmpty()) {
                        UniversityListUiState.EmptyListState
                    } else {
                        combine(
                            flowOf(universitiesResponse),
                            userPreferencesFlow
                        ) { allUniversities, userPreferences ->
                            // Combine the results and emit a new UniversityListUiState
                            val sortedUniversities = sortUniversities(
                                allUniversities,
                                userPreferences.sortOrder
                            )
                            _localUniversities = sortedUniversities
                            return@combine UniversityListUiState.ListState(
                                universitiesList = sortedUniversities,
                                sortOrder = userPreferences.sortOrder
                            )
                        }.onEach {
                            _universityUiState.update { _ ->
                                UniversityListUiState.ListState(
                                    universitiesList = it.universitiesList,
                                    sortOrder = it.sortOrder
                                )
                            }
                        }.launchIn(this)
                    }
                }

                is NetworkResult.Error -> {
                    _universityUiState.update {
                        UniversityListUiState.ErrorState(
                            failureMessage = result.message
                        )
                    }
                }

                is NetworkResult.Exception -> {
                    _universityUiState.update {
                        UniversityListUiState.ErrorState(
                            failureMessage = result.e.message
                        )
                    }
                }
            }
        }
    }

    /**
     * Sorts the employees based on the Type defined as the order of [Employee.employeeType].
     */
    private fun sortUniversities(
        universities: List<University>,
        sortOrder: SortOrder
    ): List<University> {
        return when (sortOrder) {
            SortOrder.NONE -> universities
            SortOrder.BY_UNIVERSITY_NAME -> universities.sortedWith(compareBy { it.name })
            SortOrder.BY_UNIVERSITY_TYPE -> universities
        }
    }

    fun enableSortByUniversityName(enable: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.enableSortByUniversityName(enable)
        }
    }

    /**
     * Filter Universities cached locally using provided query.
     */
    fun filterUniversities(query: String) {
        val filteredUniversities = if (query.trim().isEmpty()) {
            _localUniversities
        } else {
            _localUniversities.filter { university ->
                university.let {
                    it.state?.contains(query, ignoreCase = true) == true ||
                            it.name?.contains(query, ignoreCase = true) == true ||
                            it.country?.contains(query, ignoreCase = true) == true
                }
            }
        }
        Log.i("~!@#", "filterUniversities: $filteredUniversities")
        _universityUiState.update { _ ->
            UniversityListUiState.ListState(
                universitiesList = filteredUniversities,
                sortOrder = SortOrder.NONE
            )
        }
    }
}

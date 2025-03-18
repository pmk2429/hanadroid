package com.example.hanadroid.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hanadroid.model.DetailedCountry
import com.example.hanadroid.model.SimpleCountry
import com.example.hanadroid.usecases.GetCountriesUseCase
import com.example.hanadroid.usecases.GetCountryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountriesViewModel @Inject constructor(
    private val getCountryUseCase: GetCountryUseCase,
    private val getCountriesUseCase: GetCountriesUseCase
) : ViewModel() {

    private var _countriesState = MutableStateFlow(CountriesState())
    val countriesUiState = _countriesState.asStateFlow()

    init {
        updateCountryState()
    }

    private fun updateCountryState() {
        viewModelScope.launch {
            _countriesState.update {
                it.copy(
                    isLoading = true
                )
            }
            _countriesState.update {
                it.copy(
                    countries = getCountriesUseCase.execute(),
                    isLoading = false
                )
            }
        }
    }

    fun selectCountry(code: String) {
        viewModelScope.launch {
            _countriesState.update {
                it.copy(
                    selectedCountry = getCountryUseCase.execute(code)
                )
            }
        }
    }

    fun dismissCountryDialog() {
        _countriesState.update {
            it.copy(
                selectedCountry = null
            )
        }
    }

    data class CountriesState(
        val countries: List<SimpleCountry> = emptyList(),
        val isLoading: Boolean = false,
        val selectedCountry: DetailedCountry? = null
    )
}

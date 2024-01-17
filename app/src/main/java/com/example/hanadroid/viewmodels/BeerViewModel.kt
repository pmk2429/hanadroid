package com.example.hanadroid.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.load.engine.Resource
import com.example.hanadroid.networking.ResponseWrapper
import com.example.hanadroid.repository.BeerDataRepository
import com.example.hanadroid.ui.uistate.BeerDataUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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

    /*
    private fun fetchBothText() = viewModelScope.launch {
        // posting loading state in this livedata for UI
        _exampleText.postValue(Resource.Loading())

        exampleRepo.getFirstText()
            // Here, we're calling zip function
            .zip(exampleRepo.getSecondText()) { firstText, secondText ->
                // Here we're getting result of both network calls
                // i.e. firstText & secondText

                // Just returning both the values by simply
                // adding them in this lambda
                // function of zip and then we'll get this
                // result in collect function
                // Whatever we returns from here by specifying it,
                // we can collect that in collect function
                return@zip "$firstText $secondText"
            }
            // Making it run on Dispathers.IO
            // i.e. input/output thread
            .flowOn(Dispatchers.IO)
            .catch { e ->
                // Here, we'll get an exception if
                // any failure occurs in network calls
                // So, we're simply posting error message
                _exampleText.postValue(Resource.Error(e.toString()))
            }
            .collect { it ->
                // Now here we can collect that value
                // which we have passed in lambda
                // function of zip i.e. "$firstText $secondText"
                // Now simply returning result value as a single
                // item/value by wrapping it in Resource.Success class
                _exampleText.postValue(Resource.Success(it))
            }
    }
    */
}

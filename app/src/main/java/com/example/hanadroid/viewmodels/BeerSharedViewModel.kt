package com.example.hanadroid.viewmodels

import androidx.lifecycle.ViewModel
import com.example.hanadroid.model.BeerInfo
import com.example.hanadroid.ui.uistate.BeerItemUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class BeerSharedViewModel @Inject constructor(

) : ViewModel() {

    private var _selectedBeerItem = MutableStateFlow(BeerItemUiState(beerInfo = BeerInfo()))
    val selectedBeerItem: StateFlow<BeerItemUiState> get() = _selectedBeerItem

    fun setSelectedBeerItem(selectedBeerItem: BeerInfo) {
        _selectedBeerItem.update { currentUiState ->
            currentUiState.copy(beerInfo = selectedBeerItem)
        }
    }

}

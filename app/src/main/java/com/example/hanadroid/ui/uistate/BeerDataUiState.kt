package com.example.hanadroid.ui.uistate

import com.example.hanadroid.model.BeerInfo

data class BeerDataUiState(
    val beerList: List<BeerInfo> = emptyList(),
    val isLoading: Boolean = false,
    val failureMessage: String? = null,
    val onBeerInfoClick: (() -> Unit)? = null
)

package com.example.hanadroid.ui.uistate

data class DogsUiState(
    val woofImageUrl: String? = null,
    val ceoImageUrl: String? = null,
    val isLoading: Boolean = false,
    val failureMessage: String? = null
)

package com.example.hanadroid.ui.uistate

import com.example.hanadroid.model.AppError

data class BoredActivityUiState(
    val name: String = "",
    val type: String = "",
    val participants: Int = 0,
    val price: Double = 0.0,
    val url: String = "",
    val isLoading: Boolean = false,
    val failureMessage: String? = null,
    val appError: AppError? = null
)

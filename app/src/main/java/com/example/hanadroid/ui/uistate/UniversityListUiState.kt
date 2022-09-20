package com.example.hanadroid.ui.uistate

import com.example.hanadroid.data.model.University

data class UniversityListUiState(
    val universities: List<University> = emptyList(),
    val isLoading: Boolean = false,
    val failureMessage: String? = null
)

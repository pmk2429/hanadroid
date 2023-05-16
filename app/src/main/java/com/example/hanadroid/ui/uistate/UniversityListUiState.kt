package com.example.hanadroid.ui.uistate

import com.example.hanadroid.model.University

data class UniversityListUiState(
    val universities: List<University> = emptyList(),
    val isLoading: Boolean = false,
    val failureMessage: String? = null,
    val onUniversityClick: (() -> Unit)? = null
)

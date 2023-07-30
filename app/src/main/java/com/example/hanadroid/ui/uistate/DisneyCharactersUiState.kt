package com.example.hanadroid.ui.uistate

import com.example.hanadroid.model.DisneyCharacter

data class DisneyCharactersUiState(
    var characters: List<DisneyCharacter> = emptyList(),
    val isLoading: Boolean = false,
    val failureMessage: String? = null
)

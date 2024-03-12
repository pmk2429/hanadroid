package com.example.hanadroid.model.uistates

import com.example.hanadroid.model.DisneyCharacter

data class DisneyCharactersUiState(
    val characters: List<DisneyCharacter> = emptyList(),
    val isLoading: Boolean = false,
    val failureMessage: String? = null,
    val hasNextPage: Boolean = true
)

package com.example.hanadroid.model.uistates

import com.hana.nextdoor.models.Story

data class StoryUiState(
    val stories: List<Story> = emptyList(),
    val failureMessage: String? = null,
    val isLoading: Boolean = false,
    val hasNextPage: Boolean = true
)

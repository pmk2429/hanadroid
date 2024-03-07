package com.hana.nextdoor.models

data class StoryUiState(
    val stories: List<Story> = emptyList(),
    val failureMessage: String? = null,
    val isLoading: Boolean = false,
    val hasNextPage: Boolean = true
)

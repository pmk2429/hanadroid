package com.example.hanadroid.ui.uistate

import com.example.hanadroid.model.RedditPost

class PassengerUiState(
    val passengers: List<RedditPost> = emptyList(),
    val isLoading: Boolean = false,
    val failureMessage: String? = null
)

/**
 * UiModel to feed in to the PagingAdapter for displaying [RedditPost].
 */
sealed class RedditPostUiModel {
    data class RepoItem(val repo: RedditPost) : RedditPostUiModel()
    data class SeparatorItem(val description: String) : RedditPostUiModel()
}

sealed class UiAction {
    data class Search(val query: String) : UiAction()
    data class Scroll(val currentQuery: String) : UiAction()
}

data class UiState(
    val hasNotScrolledForCurrentSearch: Boolean = false
)

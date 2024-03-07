package com.example.hanadroid.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hanadroid.networking.NetworkResult
import com.example.hanadroid.repository.NewsFeedRepository
import com.example.hanadroid.util.toStory
import com.hana.nextdoor.models.Story
import com.hana.nextdoor.models.StoryUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsFeedViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val newsFeedRepository: NewsFeedRepository
) : ViewModel() {

    private var getNewsFeedJob: Job? = null

    private var _storyFeedUiState = MutableStateFlow(StoryUiState(isLoading = true))
    val storyFeedUiState: StateFlow<StoryUiState> get() = _storyFeedUiState

    private var _nextPageId: String? = null

    private var storiesList = mutableListOf<Story>()

    init {
        fetchNewsFeed()
    }

    private fun fetchNewsFeed(pageId: String? = "1") {
        if (getNewsFeedJob?.isActive == true) {
            return
        }
        getNewsFeedJob = viewModelScope.launch {
            val response = newsFeedRepository.fetchNewsFeed(pageId)
            when (response) {
                is NetworkResult.Success -> {
                    val pageId: String? = response.data.nextPage
                    _nextPageId = pageId
                    val uiStories = response.data.storiesFeed.map { it.toStory() }
                    storiesList.addAll(uiStories)
                    _storyFeedUiState.update { currUiState ->
                        currUiState.copy(
                            stories = storiesList,
                            isLoading = false,
                            hasNextPage = pageId != null
                        )
                    }
                }

                is NetworkResult.Error -> {
                    _storyFeedUiState.update { currUiState ->
                        currUiState.copy(
                            failureMessage = response.message,
                            isLoading = false
                        )
                    }
                }

                is NetworkResult.Exception -> {
                    _storyFeedUiState.update { currUiState ->
                        currUiState.copy(
                            failureMessage = response.e.message,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    // 1, 2, 3
    fun loadNextPage() {
        _storyFeedUiState.update { currUiState ->
            currUiState.copy(
                isLoading = true
            )
        }
        fetchNewsFeed(_nextPageId)
    }

}

package com.example.hanadroid.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.hanadroid.model.RedditPost
import com.example.hanadroid.repository.RedditRepository
import com.example.hanadroid.ui.uistate.UiAction
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class RedditPostsViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val repository: RedditRepository,
) : ViewModel() {

    val pagingDataFlow: Flow<PagingData<RedditPost>>

    val accept: (UiAction) -> Unit

    init {
        val actionStateFlow = MutableSharedFlow<UiAction>()
        val searches = actionStateFlow
            .filterIsInstance<UiAction.Search>()
            .distinctUntilChanged()
            .onStart { emit(UiAction.Search("")) }

        val queriesScrolled = actionStateFlow
            .filterIsInstance<UiAction.Scroll>()
            .distinctUntilChanged()
            // This is shared to keep the flow "hot" while caching the last query scrolled,
            // otherwise each flatMapLatest invocation would lose the last query scrolled,
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                replay = 1
            )
            .onStart { emit(UiAction.Scroll(currentQuery = "")) }

        pagingDataFlow = fetchPosts()

        accept = { action ->
            viewModelScope.launch { actionStateFlow.emit(action) }
        }
    }

    private fun fetchPosts(): Flow<PagingData<RedditPost>> =
        repository
            .getRedditPosts()
            .cachedIn(viewModelScope)
}

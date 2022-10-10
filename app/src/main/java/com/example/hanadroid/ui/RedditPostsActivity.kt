package com.example.hanadroid.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.hanadroid.adapters.RedditPostAdapter
import com.example.hanadroid.adapters.RedditPostsLoadStateAdapter
import com.example.hanadroid.databinding.ActivityRedditPostsBinding
import com.example.hanadroid.model.RedditPost
import com.example.hanadroid.ui.uistate.UiAction
import com.example.hanadroid.ui.uistate.UiState
import com.example.hanadroid.viewmodels.HanaViewModelFactory
import com.example.hanadroid.viewmodels.RedditPostsViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RedditPostsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRedditPostsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRedditPostsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // add dividers between RecyclerView's row items
        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        binding.redditPosts.addItemDecoration(decoration)

        val viewModel: RedditPostsViewModel by viewModels {
            HanaViewModelFactory(this, savedInstanceState, this)
        }

        // bind the state
        binding.bindState(
            pagingData = viewModel.pagingDataFlow,
            uiActions = viewModel.accept
        )
    }

    /**
     * Binds the [UiState] provided  by the [SearchRepositoriesViewModel] to the UI,
     * and allows the UI to feed back user actions to it.
     */
    private fun ActivityRedditPostsBinding.bindState(
        pagingData: Flow<PagingData<RedditPost>>,
        uiActions: (UiAction) -> Unit
    ) {
        val redditPostAdapter = RedditPostAdapter()
        val postsLoadAdapter = RedditPostsLoadStateAdapter { redditPostAdapter.retry() }
        redditPosts.adapter = redditPostAdapter.withLoadStateHeaderAndFooter(
            header = postsLoadAdapter,
            footer = postsLoadAdapter
        )
        bindList(
            header = postsLoadAdapter,
            redditPostAdapter = redditPostAdapter,
            pagingData = pagingData,
            onScrollChanged = uiActions
        )
    }

    private fun ActivityRedditPostsBinding.bindList(
        header: RedditPostsLoadStateAdapter,
        redditPostAdapter: RedditPostAdapter,
        pagingData: Flow<PagingData<RedditPost>>,
        onScrollChanged: (UiAction.Scroll) -> Unit
    ) {

        lifecycleScope.launch {
            pagingData.collectLatest(redditPostAdapter::submitData)
        }


        lifecycleScope.launch {
            redditPostAdapter.loadStateFlow.collect { loadState ->
                // Show a retry header if there was an error refreshing, and items were previously
                // cached OR default to the default prepend state
                header.loadState = loadState.mediator
                    ?.refresh
                    ?.takeIf { it is LoadState.Error && redditPostAdapter.itemCount > 0 }
                    ?: loadState.prepend

                val isListEmpty =
                    loadState.refresh is LoadState.NotLoading && redditPostAdapter.itemCount == 0
                emptyList.isVisible = isListEmpty
                // Only show the list if refresh succeeds, either from the the local db or the remote.
                redditPosts.isVisible =
                    loadState.source.refresh is LoadState.NotLoading || loadState.mediator?.refresh is LoadState.NotLoading
                // Show loading spinner during initial load or refresh.
                progressBar.isVisible = loadState.mediator?.refresh is LoadState.Loading
                // Show the retry state if initial load or refresh fails.
                retryButton.isVisible =
                    loadState.mediator?.refresh is LoadState.Error && redditPostAdapter.itemCount == 0
                // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
                val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
                errorState?.let {
                    Toast.makeText(
                        this@RedditPostsActivity,
                        "\uD83D\uDE28 Wooops ${it.error}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}

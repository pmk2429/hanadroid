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
import androidx.recyclerview.widget.RecyclerView
import com.example.hanadroid.adapters.CharactersAdapter
import com.example.hanadroid.adapters.CharactersLoadStateAdapter
import com.example.hanadroid.databinding.ActivityRickAndMortyMainBinding
import com.example.hanadroid.model.RickMortyCharacter
import com.example.hanadroid.viewmodels.HanaViewModelFactory
import com.example.hanadroid.viewmodels.RickAndMortyViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

class RickAndMortyCharactersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRickAndMortyMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRickAndMortyMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: RickAndMortyViewModel by viewModels {
            HanaViewModelFactory(this, savedInstanceState, this)
        }

        // add dividers between RecyclerView's row items
        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        binding.recyclerViewCharacters.addItemDecoration(decoration)

        binding.bindList(viewModel.charactersFlow)
    }

    /**
     * Binds the [UiState] provided  by the [RickAndMortyViewModel] to the UI,
     * and allows the UI to feed back user actions to it.
     */
    private fun ActivityRickAndMortyMainBinding.bindList(
        pagingData: Flow<PagingData<RickMortyCharacter>>,
    ) {
        val charactersAdapter = CharactersAdapter()
        charactersAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        val loadStateAdapter = CharactersLoadStateAdapter { charactersAdapter.retry() }
        recyclerViewCharacters.adapter = charactersAdapter.withLoadStateHeaderAndFooter(
            header = loadStateAdapter,
            footer = loadStateAdapter
        )

        // submit data to Adapter for rendering
        lifecycleScope.launch {
            pagingData.collectLatest(charactersAdapter::submitData)
        }

        // scroll to 0th position
        lifecycleScope.launch {
            charactersAdapter.loadStateFlow.distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.NotLoading }
                .collect { recyclerViewCharacters.scrollToPosition(0) }
        }

        lifecycleScope.launch {
            charactersAdapter.loadStateFlow.collect { loadState ->
                // Show a retry header if there was an error refreshing, and items were previously
                // cached OR default to the default prepend state
                loadStateAdapter.loadState = loadState.mediator
                    ?.refresh
                    ?.takeIf { it is LoadState.Error && charactersAdapter.itemCount > 0 }
                    ?: loadState.prepend

                val isListEmpty =
                    loadState.refresh is LoadState.NotLoading && charactersAdapter.itemCount == 0
                // show empty list
                emptyList.isVisible = isListEmpty
                // Only show the list if refresh succeeds, either from the the local db or the remote.
                recyclerViewCharacters.isVisible =
                    loadState.source.refresh is LoadState.NotLoading || loadState.mediator?.refresh is LoadState.NotLoading
                // Show loading spinner during initial load or refresh.
                progressIndicator.isVisible = loadState.mediator?.refresh is LoadState.Loading
                // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
                val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
                errorState?.let {
                    Toast.makeText(
                        this@RickAndMortyCharactersActivity,
                        "\uD83D\uDE28 Wooops ${it.error}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}

package com.example.hanadroid.ui.fragments

import android.nfc.tech.MifareUltralight.PAGE_SIZE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hanadroid.adapters.DisneyCharactersAdapter
import com.example.hanadroid.databinding.FragmentDisneyCharactersBinding
import com.example.hanadroid.model.uistates.DisneyCharactersUiState
import com.example.hanadroid.sharedprefs.SortOrder
import com.example.hanadroid.viewmodels.DisneyCharactersViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DisneyCharactersListFragment : Fragment() {

    private var _binding: FragmentDisneyCharactersBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var disneyCharactersAdapter: DisneyCharactersAdapter

    private val disneyCharactersViewModel: DisneyCharactersViewModel by viewModels()

    private var hasNextPage = false
    private var isLoading = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDisneyCharactersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        disneyCharactersViewModel.refreshCharactersData()

        disneyCharactersAdapter.characterItemClickListener.onItemClick = {
            // favorite character
            disneyCharactersViewModel.favoriteCharacter(it)
        }

        disneyCharactersViewModel.initialSetupEvent.observe(viewLifecycleOwner) { initialSetupEvent ->
            updateSortFilters(initialSetupEvent.sortOrder)
            setupOnCheckChangeListener()
        }

        binding.bindAdapterAndContent(disneyCharactersViewModel.disneyCharactersUiState)
    }

    private fun FragmentDisneyCharactersBinding.bindAdapterAndContent(
        disneyCharactersUiState: StateFlow<DisneyCharactersUiState>
    ) {
        swipeRefreshCharacters.setOnRefreshListener {
            disneyCharactersViewModel.refreshCharactersData()
        }
        disneyCharactersRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = disneyCharactersAdapter

            lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    disneyCharactersUiState.collect { uiState ->
                        hasNextPage = uiState.hasNextPage
                        uiState.apply {
                            if (isLoading) {
                                swipeRefreshCharacters.isRefreshing = false
                                toggleViews(loadingSpinnerVisible = true)
                            } else if (!failureMessage.isNullOrEmpty()) {
                                toggleViews(errorVisible = true)
                            } else {
                                toggleViews(recyclerViewVisible = true)
                                disneyCharactersAdapter.submitList(characters)
                            }
                        }
                    }
                }
            }

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    (layoutManager as LinearLayoutManager).apply {
                        val visibleItemCount = this.childCount
                        val totalItemCount = this.itemCount
                        val firstVisibleItemPosition = this.findFirstVisibleItemPosition()
                        if (hasNextPage && !isLoading) {
                            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                                && firstVisibleItemPosition >= 0
                                && totalItemCount >= PAGE_SIZE
                            ) {
                                isLoading = true
                                loadMoreData()
                            }
                        }
                    }
                }
            })
        }
    }

    private fun FragmentDisneyCharactersBinding.toggleViews(
        loadingSpinnerVisible: Boolean = false,
        recyclerViewVisible: Boolean = false,
        errorVisible: Boolean = false
    ) {
        progressIndicator.isVisible = loadingSpinnerVisible
        disneyCharactersRecyclerView.isVisible = recyclerViewVisible
    }

    private fun loadMoreData() {
        disneyCharactersViewModel.loadMoreData()
        isLoading = false
    }

    private fun updateSortFilters(sortOrder: SortOrder) {
        with(binding) {
            sortMovies.isChecked =
                sortOrder == SortOrder.MOVIES || sortOrder == SortOrder.MOVIES_AND_SHOWS
            sortShows.isChecked =
                sortOrder == SortOrder.TV_SHOWS || sortOrder == SortOrder.MOVIES_AND_SHOWS
        }
    }

    private fun setupOnCheckChangeListener() {
        with(binding) {
            sortMovies.setOnCheckedChangeListener { _, checked ->
                disneyCharactersViewModel.enableSortByMovies(checked)
            }
            sortShows.setOnCheckedChangeListener { _, checked ->
                disneyCharactersViewModel.enableSortByTvShows(checked)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

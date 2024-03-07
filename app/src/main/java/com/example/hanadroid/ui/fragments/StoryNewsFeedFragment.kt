package com.example.hanadroid.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hanadroid.adapters.StoryNewsFeedAdapter
import com.example.hanadroid.databinding.FragmentNewsFeedBinding
import com.example.hanadroid.viewmodels.NewsFeedViewModel
import com.hana.nextdoor.models.StoryUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class StoryNewsFeedFragment : Fragment() {

    private var _binding: FragmentNewsFeedBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var storyNewsFeedAdapter: StoryNewsFeedAdapter

    private val newsFeedViewModel: NewsFeedViewModel by viewModels()

    private var hasNextPage = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bindAdapterAndContent(newsFeedViewModel.storyFeedUiState)
    }

    private fun FragmentNewsFeedBinding.bindAdapterAndContent(
        storyFeedUiState: StateFlow<StoryUiState>
    ) {
        storyFeedRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = storyNewsFeedAdapter

            lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    storyFeedUiState.collect { uiState ->
                        hasNextPage = uiState.hasNextPage
                        Log.i("~!@#", "Next Page Id: $")
                        uiState.failureMessage?.let {
                            Log.i("~!@#", it)
                        } ?: also {
                            storyNewsFeedAdapter.submitList(uiState.stories)
                        }
                    }
                }
            }

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    (layoutManager as LinearLayoutManager).apply {
                        val visibleItemCount: Int = this.childCount
                        val totalItemCount: Int = this.itemCount
                        val firstVisibleItemPosition: Int = this.findFirstVisibleItemPosition()
                        if (hasNextPage) {
                            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                                newsFeedViewModel.loadNextPage()
                            }
                        }
                    }
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

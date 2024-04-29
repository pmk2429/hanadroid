package com.example.hanadroid.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader
import com.bumptech.glide.util.FixedPreloadSizeProvider
import com.example.hanadroid.R
import com.example.hanadroid.adapters.BeerDataListAdapter
import com.example.hanadroid.databinding.FragmentBeerDataListBinding
import com.example.hanadroid.ui.recyclerView.CombinedScrollListener
import com.example.hanadroid.ui.recyclerView.MyPreloadModelProvider
import com.example.hanadroid.ui.uistate.BeerDataUiState
import com.example.hanadroid.util.PaginationScrollListener
import com.example.hanadroid.viewmodels.BeerSharedViewModel
import com.example.hanadroid.viewmodels.BeerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class BeerListFragment : Fragment() {

    private var _binding: FragmentBeerDataListBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var beerDataListAdapter: BeerDataListAdapter

    private val beerViewModel: BeerViewModel by viewModels()
    private val beerSharedViewModel: BeerSharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBeerDataListBinding.inflate(inflater, container, false)
        binding.bindAdapter(beerViewModel.beerDataUiState)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        beerDataListAdapter.itemClickListener.onItemClick = {
            beerSharedViewModel.setSelectedBeerItem(it)
            findNavController().navigate(R.id.action_beerList_to_beerDetails)
        }
    }

    private fun FragmentBeerDataListBinding.bindAdapter(
        beerDataUiState: StateFlow<BeerDataUiState>
    ) {
        beerRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
            beerDataListAdapter.stateRestorationPolicy =
                RecyclerView.Adapter.StateRestorationPolicy.ALLOW
            adapter = beerDataListAdapter

            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    beerDataUiState.collect { uiState ->
                        beerDataListAdapter.submitList(uiState.beerList)
                    }
                }
            }

            // For Preloading Images using Glide
            val sizeProvider = FixedPreloadSizeProvider<String>(0, 0)
            val modelProvider = MyPreloadModelProvider(requireContext())
            val preloader = RecyclerViewPreloader(
                Glide.with(this@BeerListFragment), modelProvider, sizeProvider, 10 /*maxPreload*/
            )

            // For Pagination
            val paginationScrollListener = object : PaginationScrollListener(layoutManager as LinearLayoutManager) {
                    override fun loadMoreItems() {
                        // fetch more items
                    }

                    override fun isLastPage() = false

                    override fun isLoading() = false
                }

            val combinedListener = CombinedScrollListener(
                listOf(
                    paginationScrollListener,
                    preloader
                )
            )

            // Add Combined Listeners to the RecyclerView
            addOnScrollListener(combinedListener)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

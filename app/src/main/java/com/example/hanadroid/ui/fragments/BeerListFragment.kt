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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hanadroid.adapters.BeerDataListAdapter
import com.example.hanadroid.databinding.FragmentBeerDataListBinding
import com.example.hanadroid.ui.uistate.BeerDataUiState
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBeerDataListBinding.inflate(inflater, container, false)
        binding.bindAdapter(beerViewModel.beerDataUiState)
        return binding.root
    }

    private fun FragmentBeerDataListBinding.bindAdapter(beerDataUiState: StateFlow<BeerDataUiState>) {
        beerRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
            beerDataListAdapter.stateRestorationPolicy =
                RecyclerView.Adapter.StateRestorationPolicy.ALLOW
            adapter = beerDataListAdapter

            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    beerDataUiState.collect { uiState ->
                        Log.i("~!@#$", " --- " + uiState.beerList)
                        beerDataListAdapter.submitList(uiState.beerList)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

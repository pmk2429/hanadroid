package com.example.hanadroid.ui.fragments

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
import com.example.hanadroid.adapters.MarsDataListAdapter
import com.example.hanadroid.databinding.FragmentMarsDataListBinding
import com.example.hanadroid.ui.uistate.MarsDataUiState
import com.example.hanadroid.viewmodels.MarsDataViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MarsDataListFragment : Fragment() {

    private var _binding: FragmentMarsDataListBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var marsDataListAdapter: MarsDataListAdapter

    private val marsDataViewModel: MarsDataViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMarsDataListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bindAdapterAndContent(marsDataViewModel.marsDataUiState)
    }

    private fun FragmentMarsDataListBinding.bindAdapterAndContent(marsDataUiState: StateFlow<MarsDataUiState>) {
        marsDataRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = marsDataListAdapter

            lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    marsDataUiState.collect { uiState ->
                        if (uiState.isLoading) {
                            loadingProgress.isVisible = true
                        } else {
                            marsDataListAdapter.submitList(uiState.marsDataList)
                        }
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
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
import com.example.hanadroid.databinding.FragmentRandomBoredActivityBinding
import com.example.hanadroid.ui.viewmodels.BoredActivityViewModel
import com.example.hanadroid.ui.viewmodels.HanaViewModelFactory
import kotlinx.coroutines.launch

class BoredActivityFragment @JvmOverloads constructor(
    factoryProducer: (() -> HanaViewModelFactory)? = null
) : Fragment() {

    private var _binding: FragmentRandomBoredActivityBinding? = null
    private val binding get() = _binding!!

    private val boredActivityViewModel: BoredActivityViewModel by viewModels(
        factoryProducer = factoryProducer ?: {
            HanaViewModelFactory(this, arguments)
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRandomBoredActivityBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = boredActivityViewModel

        setupObservers()

        return binding.root
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                boredActivityViewModel.boredActivityUiState.collect { uiState ->
                    Log.i("~!@#", "SUCCESS --> ${uiState.name}")
                    Log.i("~!@#", "LOADING --> ${uiState.isLoading}")
                    Log.i("~!@#", "ERROR --> ${uiState.failureMessage}")
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}

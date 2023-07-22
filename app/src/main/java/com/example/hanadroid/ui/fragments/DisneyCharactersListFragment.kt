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
import com.example.hanadroid.adapters.DisneyCharactersAdapter
import com.example.hanadroid.databinding.FragmentDisneyCharactersBinding
import com.example.hanadroid.ui.uistate.DisneyCharactersUiState
import com.example.hanadroid.viewmodels.DisneyCharactersViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DisneyCharactersListFragment : Fragment() {

    private var _binding: FragmentDisneyCharactersBinding? = null
    private val binding get() = _binding!!

    private val disneyCharactersViewModel: DisneyCharactersViewModel by viewModels()

    @Inject
    lateinit var disneyCharactersAdapter: DisneyCharactersAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDisneyCharactersBinding.inflate(inflater, container, false)

        binding.bindList(disneyCharactersViewModel.disneyCharactersUiState)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun FragmentDisneyCharactersBinding.bindList(disneyCharactersUiState: StateFlow<DisneyCharactersUiState>) {
        disneyCharactersRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = disneyCharactersAdapter

            lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    disneyCharactersUiState.collect { uiState ->
                        Log.i("~!@#", "SUCCESS --> ${uiState.characters}")
                        Log.i("~!@#", "LOADING --> ${uiState.isLoading}")
                        Log.i("~!@#", "ERROR --> ${uiState.failureMessage}")
                        disneyCharactersAdapter.submitList(uiState.characters)
                    }
                }
            }
        }
    }
}

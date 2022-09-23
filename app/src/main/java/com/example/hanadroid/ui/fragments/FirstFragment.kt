package com.example.hanadroid.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hanadroid.R
import com.example.hanadroid.data.model.University
import com.example.hanadroid.databinding.FragmentFirstBinding
import com.example.hanadroid.ui.adapters.UniversityAdapter
import com.example.hanadroid.ui.viewmodels.UniversityListViewModel
import com.example.hanadroid.ui.viewmodels.HanaViewModelFactory
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment(), UniversityAdapter.UniversityItemClickListener {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    private lateinit var universityAdapter: UniversityAdapter

    private val universityListViewModel: UniversityListViewModel by viewModels {
        HanaViewModelFactory(this, arguments)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)

        setupUI()
        setupObserver()

        return binding.root

    }

    private fun setupUI() {
        binding.universityRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            universityAdapter = UniversityAdapter(emptyList(), this@FirstFragment)
            addItemDecoration(
                DividerItemDecoration(context, (layoutManager as LinearLayoutManager).orientation)
            )
            adapter = universityAdapter
        }
    }

    private fun setupObserver() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                universityListViewModel.universityUiState.collect { uiState ->
                    Log.i("~!@#", "SUCCESS --> ${uiState.universities}")
                    renderUniversities(uiState.universities)
                    Log.i("~!@#", "LOADING --> ${uiState.isLoading}")
                    Log.i("~!@#", "ERROR --> ${uiState.failureMessage}")
                }
            }
        }
    }

    private fun renderUniversities(universities: List<University>) {
        universityAdapter.setUniversities(universities)
    }
    
    private fun hideSoftKeyboard() {
        val view = requireActivity().currentFocus
        if (view != null) {
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onUniversityClicked(university: University) {
        findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
    }
}
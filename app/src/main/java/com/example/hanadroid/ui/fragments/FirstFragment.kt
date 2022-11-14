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
import androidx.recyclerview.widget.RecyclerView
import com.example.hanadroid.R
import com.example.hanadroid.adapters.UniversityAdapter
import com.example.hanadroid.databinding.FragmentFirstBinding
import com.example.hanadroid.model.University
import com.example.hanadroid.viewmodels.HanaViewModelFactory
import com.example.hanadroid.viewmodels.UniversityListViewModel
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

        binding.bindAdapter(universityAdapter = UniversityAdapter(emptyList(), this@FirstFragment))

        setupObserver()

        return binding.root

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

private fun FragmentFirstBinding.bindAdapter(universityAdapter: UniversityAdapter) {
    universityRecyclerView.apply {
        layoutManager = object : LinearLayoutManager(context) {
            override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
                // force height of viewHolder here, this will override layout_height from xml
                lp.height = height / 3
                return true
            }
        }
        addItemDecoration(
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        )
        adapter = universityAdapter
    }
}
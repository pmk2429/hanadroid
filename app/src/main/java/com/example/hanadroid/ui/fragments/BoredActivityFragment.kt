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
import com.example.hanadroid.ui.views.SingleRowView
import com.example.hanadroid.util.*
import com.example.hanadroid.viewmodels.BoredActivityViewModel
import com.example.hanadroid.viewmodels.HanaViewModelFactory
import kotlinx.coroutines.launch

/**
 * Fragment that fetches and displays Bored Random Activities.
 */
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listAllChildViewsOfType(binding.boredActivityRowsContainer)
        listAllDirectChildren(binding.boredActivityContainer)
        //listAllChildViews(binding.boredActivityContainer)
        //testExtensions()
        testLambda()
    }

    private fun testExtensions() {
        val demo = "Pavitra"
        val first = demo.findFirstIndex { t -> t == 'a' }
        Log.i("~!@#", "$first")
        val last = demo.findLastIndex { t -> t == 'a' }
        Log.i("~!@#", "$last")
    }

    private fun testLambda() {
        val summation = sum(10, 20, addTwoNumbers())
        println(summation)
    }

    /** val lambdaName : () -> ReturnType = { args1, args2, ... -> codeBody } */
    private fun addTwoNumbers(): (Int, Int) -> Int = { first, second -> first + second }

    private fun sum(
        x: Int,
        y: Int,
        action: (Int, Int) -> Int // Notice how we are passing the lambda as a parameter to sum() method.
    ) = action(x, y)

    private fun setupObservers() {
        lifecycleScope.launch {
            // The block passed to repeatOnLifecycle is executed when the lifecycle
            // is at least STARTED and is cancelled when the lifecycle is STOPPED.
            // It automatically restarts the block when the lifecycle is STARTED again.
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Safely collect from boredActivityUiState (StateFlow) when the lifecycle is STARTED
                // and stops collection when the lifecycle is STOPPED
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

    private fun listAllChildViewsOfType(parent: ViewGroup) {
        val singleRowViews = parent.getViewsByType(SingleRowView::class.java)
        for (view in singleRowViews) {
            Log.i("~!@#", view.toString())
        }
    }

    private fun listAllDirectChildren(parent: ViewGroup) {
        val allViews = parent.allDirectChildren<View>()
        for (view in allViews) {
            Log.i("~!@#", view.toString())
        }
        Log.i("~!@#", "------------------------------------------------------------------")
    }

    private fun listAllChildViews(parent: ViewGroup) {
        val allChildren = parent.allChildViews<View>()
        for (view in allChildren) {
            Log.i("~!@#", view.toString())
        }
    }

    /**
     * Same as extension function Extensions#allDirectChildren()
     */
    private fun ViewGroup.getAllChildren(): List<View> {
        val children = ArrayList<View>()
        for (i in 0 until this.childCount) {
            children.add(this.getChildAt(i))
        }
        return children
    }

}

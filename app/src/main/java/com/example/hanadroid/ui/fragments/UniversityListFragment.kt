package com.example.hanadroid.ui.fragments

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hanadroid.R
import com.example.hanadroid.adapters.UniversityAdapter
import com.example.hanadroid.databinding.FragmentUniversityListBinding
import com.example.hanadroid.model.University
import com.example.hanadroid.ui.uistate.UniversityListUiState
import com.example.hanadroid.util.launchAndRepeatWithLifecycleOwner
import com.example.hanadroid.viewmodels.UniversityListViewModel
import com.example.hanadroid.viewmodels.UniversitySharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class UniversityListFragment : Fragment() {

    private var _binding: FragmentUniversityListBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var universityAdapter: UniversityAdapter

    private val universityListViewModel: UniversityListViewModel by viewModels()

    private val sharedViewModel: UniversitySharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUniversityListBinding.inflate(inflater, container, false)

        universityAdapter.universityItemClickListener.onItemClick = {
            sharedViewModel.updateUniversity(it)
            findNavController().navigate(
                R.id.action_universityListFragment_to_SecondFragment,
                Bundle().apply {
                    putString(ARGS_UNIVERSITY_NAME, it.name)
                })
        }

        binding.bindAdapter(universityListViewModel.universityUiState)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.menu_main, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.action_search -> {
                        val searchManager =
                            requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun hideSoftKeyboard() {
        val view = requireActivity().currentFocus
        if (view != null) {
            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun FragmentUniversityListBinding.bindAdapter(universityUiState: StateFlow<UniversityListUiState>) {
        swipeRefreshLayout.setOnRefreshListener {
            universityListViewModel.fetchUniversitiesByCountry()
        }

        universityRecyclerView.apply {
//            layoutManager = object : LinearLayoutManager(context) {
//                override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
//                    // force height of viewHolder here, this will override layout_height from xml
//                    lp.height = height / 3
//                    return true
//                }
//            }
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(
                DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            )
            universityAdapter.stateRestorationPolicy =
                RecyclerView.Adapter.StateRestorationPolicy.ALLOW
            adapter = universityAdapter

            lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    universityUiState.collect { uiState ->
                        swipeRefreshLayout.isRefreshing = false
                        loadingProgress.isVisible = uiState.isLoading
                    }
                }
            }

            // Sample use of the Kotlin Coroutine Extension to launch and repeat
            launchAndRepeatWithLifecycleOwner(
                universityUiState
            ) { uiState ->
                Log.i("~!@#", "SUCCESS --> ${uiState.universities}")
                Log.i("~!@#", "LOADING --> ${uiState.isLoading}")
                Log.i("~!@#", "ERROR --> ${uiState.failureMessage}")
                universityAdapter.submitList(uiState.universities)
                setRecyclerViewItemTouchListener(
                    this@apply,
                    uiState.universities
                )
            }

            /* Smooth scroll to a position in Recycler Adapter when the data set changes
               using an AdapterDataObserver to automatically scroll the RecyclerView when items
               are added or removed from the adapter.
             */
            universityAdapter.registerAdapterDataObserver(object :
                RecyclerView.AdapterDataObserver() {
                override fun onItemRangeInserted(
                    positionStart: Int,
                    itemCount: Int
                ) {
                    this@apply.scrollToPosition(0)
                }

                override fun onItemRangeRemoved(
                    positionStart: Int,
                    itemCount: Int
                ) {
                    // Scroll to the last item after removal
                    val lastItemPosition = adapter!!.itemCount - 1
                    this@apply.smoothScrollToPosition(lastItemPosition)
                }
            })
        }
    }

    private fun setRecyclerViewItemTouchListener(
        recyclerView: RecyclerView,
        universities: List<University>
    ) {
        val itemTouchCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                val position = viewHolder.absoluteAdapterPosition
                val universitiesMain = mutableListOf(universities)
                universitiesMain.removeAt(position)
                recyclerView.adapter?.notifyItemRemoved(position)
            }
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    companion object {
        const val ARGS_UNIVERSITY_COUNTRY_KEY = "ARGS_UNIVERSITY_COUNTRY_KEY"
        const val ARGS_UNIVERSITY_NAME = "ARGS_UNIVERSITY_NAME"
    }
}

//private fun setRecyclerViewScrollListener() {
//    recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//            super.onScrollStateChanged(recyclerView, newState)
//            val totalItemCount = recyclerView.layoutManager!!.itemCount
//            if (!imageRequester.isLoadingData && totalItemCount == lastVisibleItemPosition + 1) {
//                requestPhoto()
//            }
//        }
//    })
//}
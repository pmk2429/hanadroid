package com.example.hanadroid.ui.fragments

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
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hanadroid.R
import com.example.hanadroid.adapters.UniversityAdapter
import com.example.hanadroid.databinding.FragmentUniversityListBinding
import com.example.hanadroid.model.University
import com.example.hanadroid.repository.SortOrder
import com.example.hanadroid.ui.uistate.UniversityListUiState
import com.example.hanadroid.util.launchAndRepeatWithLifecycleOwner
import com.example.hanadroid.viewmodels.UniversityListViewModel
import com.example.hanadroid.viewmodels.UniversitySharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        universityListViewModel.initialSetupEvent.observe(viewLifecycleOwner) { initialSetupEvent ->
            updateTaskFilters(initialSetupEvent.sortOrder)
            setupOnCheckedChangeListener()
        }

        // hook the Item click listener
        universityAdapter.universityItemClickListener.onItemClick = {
            sharedViewModel.updateUniversity(it)
            findNavController().navigate(
                R.id.action_universityListFragment_to_SecondFragment,
                Bundle().apply {
                    putString(ARGS_UNIVERSITY_NAME, it.name)
                })
        }

        // fetch and bind the backend data to Adapter
        binding.bindAdapter(universityListViewModel.universityUiState)

        bindMenuItem()
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

    private fun FragmentUniversityListBinding.bindAdapter(
        universityUiState: StateFlow<UniversityListUiState>
    ) {
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

            // Sample use of the Kotlin Coroutine Extension to launch and repeat
            launchAndRepeatWithLifecycleOwner(
                universityUiState
            ) { uiState ->
                when (uiState) {
                    is UniversityListUiState.LoadingState -> {
                        Log.i("~!@#", "LOADING...")
                        swipeRefreshLayout.isRefreshing = false
                        loadingProgress.isVisible = true
                    }

                    is UniversityListUiState.ListState -> {
                        loadingProgress.isVisible = false
                        Log.i("~!@#", "SUCCESS --> ${uiState.universitiesList}")
                        universityAdapter.submitList(uiState.universitiesList)
                        setRecyclerViewItemTouchListener(
                            this@apply,
                            uiState.universitiesList
                        )
                    }

                    is UniversityListUiState.ErrorState -> {
                        loadingProgress.isVisible = false
                        Log.i("~!@#", "ERROR --> ${uiState.failureMessage}")
                    }

                    is UniversityListUiState.EmptyListState -> {
                        loadingProgress.isVisible = false
                    }
                }
            }

            swipeRefreshLayout.setOnRefreshListener {
                universityListViewModel.refreshUniversitiesFetch()
            }
        }
    }

    private fun bindMenuItem() {
        // Setup Menu item
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.menu_main, menu)
                val searchView = menu.findItem(R.id.action_search).actionView as SearchView
                searchView.isSubmitButtonEnabled = true
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        if (!query.isNullOrEmpty()) {
                            Log.i("~!@#", "query: $query")
                            // get items form ViewModel and update using the query
                            universityListViewModel.filterUniversities(query)
                        }
                        return true
                    }

                    override fun onQueryTextChange(query: String?): Boolean {
                        if (!query.isNullOrEmpty()) {
                            Log.i("~!@#", "query: $query")
                        }
                        return true
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.action_search -> {
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun updateTaskFilters(sortOrder: SortOrder) {
        binding.sortUniversity.isChecked = sortOrder == SortOrder.BY_UNIVERSITY_NAME
    }

    private fun setupOnCheckedChangeListener() {
        binding.sortUniversity.setOnCheckedChangeListener { _, checked ->
            universityListViewModel.enableSortByUniversityName(checked)
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
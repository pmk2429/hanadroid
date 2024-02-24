package com.example.hanadroid.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hanadroid.adapters.FancyAdapter
import com.example.hanadroid.databinding.ActivityFancyListBinding
import com.example.hanadroid.ui.uistate.FancyItemsListUiState
import com.example.hanadroid.viewmodels.FancyListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FancyListActivity : AppCompatActivity() {

    private var _binding: ActivityFancyListBinding? = null
    val binding get() = _binding!!

    private val fancyListViewModel: FancyListViewModel by viewModels()

    private lateinit var fancyAdapter: FancyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFancyListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            fabAddFancyItem.setOnClickListener { fancyListViewModel.addRandomFancyItem() }
            fabRemoveFancyItem.setOnClickListener { fancyListViewModel.removeFancyItem() }
            bindListItems(fancyListViewModel.fancyItems)
        }
    }

    private fun ActivityFancyListBinding.bindListItems(fancyItemsUiState: MutableStateFlow<FancyItemsListUiState>) {
        recyclerViewFancyItem.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            fancyAdapter = FancyAdapter { fancyListViewModel.handleFancyItemClick() }
            adapter = fancyAdapter

            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    fancyItemsUiState.collect { uiState ->
                        fancyAdapter.submitList(uiState.fancyItems)

                        // handle item added. For this case, the item is always added to end of list
                        if (uiState.isItemAdded) {
                            handleItemInserted(uiState.fancyItems.size - 1)
                        }

                        // handle item removed
                        if (uiState.isItemRemoved) {
                            handleItemRemoved(uiState.itemRemovedAtIndex)
                        }
                    }
                }
            }

            /* Smooth scroll to a position in Recycler Adapter when the data set changes
               using an AdapterDataObserver to automatically scroll the RecyclerView when items
               are added or removed from the adapter.
             */
            fancyAdapter.registerAdapterDataObserver(
                object : RecyclerView.AdapterDataObserver() {
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
                }
            )
        }
    }

    private fun handleItemInserted(itemAddedAtIndex: Int) {
        fancyAdapter.notifyItemInserted(itemAddedAtIndex)
        // once the item is added, scroll to the bottom of the list
        binding.recyclerViewFancyItem.apply {
            postDelayed({
                smoothScrollToPosition(itemAddedAtIndex)
            }, 1000)
        }
    }

    private fun handleItemRemoved(itemRemovedAtIndex: Int) {
        fancyAdapter.notifyItemRemoved(itemRemovedAtIndex)
        // once the item is added, scroll to the bottom of the list
        binding.recyclerViewFancyItem.apply {
            postDelayed({
                smoothScrollToPosition(itemRemovedAtIndex)
            }, 1000)
        }
    }
}
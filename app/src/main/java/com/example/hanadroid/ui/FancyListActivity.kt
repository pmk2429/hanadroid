package com.example.hanadroid.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hanadroid.adapters.FancyAdapter
import com.example.hanadroid.databinding.ActivityFancyListBinding
import com.example.hanadroid.ui.uistate.FancyItemsListUiState
import com.example.hanadroid.viewmodels.FancyListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FancyListActivity : AppCompatActivity() {

    private var _binding: ActivityFancyListBinding? = null
    val binding get() = _binding!!

    private val fancyListViewModel: FancyListViewModel by viewModels()

    @Inject
    lateinit var fancyAdapter: FancyAdapter

    private var itemCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFancyListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            fabAddFancyItem.setOnClickListener { fancyListViewModel.addRandomFancyItem() }
            bindListItems(fancyListViewModel.fancyItems)
        }
    }

    private fun ActivityFancyListBinding.bindListItems(fancyItemsUiState: MutableStateFlow<FancyItemsListUiState>) {
        recyclerViewFancyItem.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = fancyAdapter

            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    fancyItemsUiState.collect { uiState ->
                        fancyAdapter.submitList(uiState.fancyItems)
                        itemCount = uiState.fancyItems.size
                        if (uiState.isItemAdded) {
                            handleItemInserted()
                        }
                    }
                }
            }
        }
    }

    private fun handleItemInserted() {
        fancyAdapter.notifyItemInserted(itemCount - 1)
        // once the item is added, scroll to the bottom of the list
        binding.recyclerViewFancyItem.apply {
            this.postDelayed({
                this.smoothScrollToPosition(this.adapter!!.itemCount - 1)
            }, 1000)
        }
    }
}
package com.example.hanadroid.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hanadroid.adapters.NonHiltListAdapter
import com.example.hanadroid.databinding.ActivityNonHiltBinding
import com.example.hanadroid.repository.NonHiltItemRepository
import com.example.hanadroid.viewmodels.NonHiltViewModel
import com.example.hanadroid.viewmodels.ViewModelFactory

class NonHiltActivity : AppCompatActivity() {

    private var _binding: ActivityNonHiltBinding? = null
    val binding get() = _binding!!

    private lateinit var itemViewModel: NonHiltViewModel
    private lateinit var itemAdapter: NonHiltListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityNonHiltBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ViewModel
        itemViewModel = ViewModelProvider(
            this,
            ViewModelFactory(NonHiltItemRepository())
        )[NonHiltViewModel::class.java]

        initAdapter()
    }

    private fun initAdapter() {
        binding.recyclerViewSimpleItem.apply {
            // Initialize RecyclerView and Adapter
            layoutManager = LinearLayoutManager(this@NonHiltActivity)
            adapter = NonHiltListAdapter()

            // Observe changes in the ViewModel
            itemViewModel.items.observe(this@NonHiltActivity) { items ->
                itemAdapter.submitList(items)
            }
        }
    }
}

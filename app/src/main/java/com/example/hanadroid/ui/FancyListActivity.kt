package com.example.hanadroid.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hanadroid.adapters.FancyAdapter
import com.example.hanadroid.databinding.ActivityFancyListBinding
import com.example.hanadroid.model.FancyModel
import com.example.hanadroid.model.getList
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FancyListActivity : AppCompatActivity() {

    private var _binding: ActivityFancyListBinding? = null
    val binding get() = _binding!!

    @Inject
    lateinit var fancyAdapter: FancyAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFancyListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            fabAddFancyItem.setOnClickListener { addNewFancyItem() }
            this.bindListItems(getList())
        }
    }

    private fun ActivityFancyListBinding.bindListItems(fancyItems: List<FancyModel>) {
        recyclerViewFancyItem.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = fancyAdapter

            fancyAdapter.submitList(fancyItems)
        }
    }

    private fun addNewFancyItem() {
        val randomInt = (0..100).random() // Generate a random integer between 0 and 100
        val updatedList = getList().toMutableList()
        updatedList.add(
            FancyModel(29, "PMK", "GOOG - best company on Earth", 1690622282119)
        )
        fancyAdapter.submitList(updatedList)
        fancyAdapter.notifyItemInserted(updatedList.size - 1)
    }

}
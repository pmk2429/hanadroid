package com.example.hanadroid.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hanadroid.adapters.FragmentAdapter
import com.example.hanadroid.databinding.ActivityTabbedFragmentBinding
import com.google.android.material.tabs.TabLayoutMediator

class TabbedFragmentActivity : AppCompatActivity() {
    private var _binding: ActivityTabbedFragmentBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityTabbedFragmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            viewPager.adapter = FragmentAdapter(this@TabbedFragmentActivity)
            // Layout Tabs using Mediator
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = "Tab ${position + 1}"
                // Enable switching tabs when clicked
                tab.view.setOnClickListener {
                    viewPager.currentItem = position
                }
            }.attach()
        }
    }
}

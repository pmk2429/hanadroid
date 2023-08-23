package com.example.hanadroid.ui

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hanadroid.databinding.ActivityWorkerBinding

class MyActivity : AppCompatActivity() {
    private var _binding: ActivityWorkerBinding? = null
    val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        _binding = ActivityWorkerBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onResume() {
        super.onResume()
    }
}
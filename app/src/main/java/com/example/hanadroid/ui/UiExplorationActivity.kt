package com.example.hanadroid.ui

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnLayout
import androidx.core.view.doOnNextLayout
import com.example.hanadroid.databinding.ActivityUiExplorationBinding

class UiExplorationActivity : AppCompatActivity() {

    private var _binding: ActivityUiExplorationBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityUiExplorationBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        Log.d("Drawing View", "onStart() executed")
        super.onStart()

        binding.textLayoutChanges.doOnLayout {
            // This block will be executed when the view is laid out on the screen
            Log.i("~!@#", "donOnLayout - TextView is laid out on the screen")
            val layoutParams = it.layoutParams
            layoutParams.height = 200 // Set new height
            layoutParams.width = 400 // Set new width
            it.layoutParams = layoutParams
        }
        binding.textLayoutChanges.setTypeface(null, Typeface.BOLD)

        binding.textLayoutChanges.doOnNextLayout {
            // This block will be executed when the view is about to be laid out again
            Log.i("~!@#", "donOnNextLayout  - TextView is about to be laid out again")
        }
    }

    override fun onResume() {
        Log.d("Drawing View", "onResume() executed")
        super.onResume()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        Log.d("Drawing View", "onWindowFocusChanged() executed")
        super.onWindowFocusChanged(hasFocus)
    }
}

package com.example.hanadroid.ui

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.hanadroid.databinding.ActivityDeepLinkBinding

class DeepLinkActivity : AppCompatActivity() {

    private var _binding: ActivityDeepLinkBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDeepLinkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Receive the intent action and data
        val action: String? = intent?.action
        val deeplinkData: Uri? = intent?.data

        Log.i("~!@#", "$action")
        Log.i("~!@#", "$deeplinkData")
    }
}
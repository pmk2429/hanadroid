package com.example.hanadroid.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hanadroid.databinding.ActivityRedditPostsBinding

class RedditPostsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRedditPostsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRedditPostsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}

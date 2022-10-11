package com.example.hanadroid.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.hanadroid.databinding.ActivityEntryBinding

class EntryActivity : AppCompatActivity() {

    private var _binding: ActivityEntryBinding? = null
    private val binding get() = _binding!!

    private val universityActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            // handle the Result
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
            }
        }

    private val paginationActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fab.apply {
            setOnTouchListener(handleTouchListener)
            setOnClickListener { launchUniversityActivity() }
        }

        binding.fabBoredActivity.apply {
            setOnTouchListener(handleTouchListener)
            setOnClickListener { launchBoredActivity() }
        }

        binding.fabPaginationArticles.apply {
            setOnClickListener { launchPaginationActivity() }
        }

        binding.fabRedditPosts.apply {
            setOnClickListener { launchRedditPostsActivity() }
        }
    }

    private fun launchUniversityActivity() {
        universityActivityLauncher.launch(Intent(this, UniversityMainActivity::class.java))
    }

    private fun launchBoredActivity() {
        val intent = Intent(this, BoredActivityLauncherActivity::class.java)
        startActivity(intent)
    }

    private fun launchPaginationActivity() {
        paginationActivityLauncher.launch(Intent(this, ArticleListPaginationActivity::class.java))
    }

    private fun launchRedditPostsActivity() {
        val intent = Intent(this, RickAndMortyCharactersActivity::class.java)
        startActivity(intent)
    }

    /**
     * This gives you the touch event coordinates relative to the view that has the touch listener
     * assigned to it. The top left corner of the view is (0, 0).
     * If you move your finger above the view, then y will be negative.
     * If you move your finger left of the view, then x will be negative.
     */
    @SuppressLint("ClickableViewAccessibility")
    private val handleTouchListener = View.OnTouchListener { _, event ->
        val x = event.x.toInt()
        val y = event.y.toInt()

        val xRelToScreen = event.rawX.toInt()
        val yRelToScreen = event.rawY.toInt()

        when (event.action) {
            MotionEvent.ACTION_DOWN -> Log.i("~!@#", "touched down ($x, $y)")
            MotionEvent.ACTION_MOVE -> Log.i("~!@#", "moving: ($x, $y)")
            MotionEvent.ACTION_UP -> Log.i("~!@#", "touched up ($x, $y)")
        }
        false
    }
}
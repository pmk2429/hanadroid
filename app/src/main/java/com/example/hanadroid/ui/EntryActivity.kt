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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fab.apply {
            setOnClickListener {
                launchUniversityActivity()
            }

            setOnTouchListener(handleTouchListener)
        }

        binding.fabBoredActivity.apply {
            setOnClickListener {
                launchBoredActivity()
            }

            setOnTouchListener(handleTouchListener)
        }
    }

    private fun launchBoredActivity() {
        val intent = Intent(this, BoredActivityLauncherActivity::class.java)
        startActivity(intent)
    }

    private fun launchUniversityActivity() {
        universityActivityLauncher.launch(Intent(this, UniversityMainActivity::class.java))
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
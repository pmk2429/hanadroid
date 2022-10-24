package com.example.hanadroid.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.hanadroid.R
import com.example.hanadroid.databinding.ActivityImageLoaderBinding
import com.google.android.material.snackbar.Snackbar

class ImageLoaderActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private var _binding: ActivityImageLoaderBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        _binding = ActivityImageLoaderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_image_loader)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAnchorView(R.id.fab)
                .setAction("Action", null).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_image_loader)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun navigateToImageDetails(view: View) {
        Navigation.findNavController(view)
            .navigate(R.id.action_loadImage_to_loadVideoGif, Bundle().apply {
                putString("DEMO", "DEMO")
            })
    }
}

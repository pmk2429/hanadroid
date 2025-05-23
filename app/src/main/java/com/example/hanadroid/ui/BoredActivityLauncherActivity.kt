package com.example.hanadroid.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.hanadroid.R
import com.example.hanadroid.databinding.ActivityBoredActivityLauncherBinding
import com.example.hanadroid.ui.EntryActivity.Companion.ACTIVITY_RESULT_CODE

class BoredActivityLauncherActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityBoredActivityLauncherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBoredActivityLauncherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_bored_activity)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onResume() {
        super.onResume()

        binding.fabGoBack.apply {
            setOnClickListener { onBoredActivityFetched() }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_bored_activity)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun onBoredActivityFetched() {
        setResult(RESULT_OK, Intent().apply {
            putExtra(ACTIVITY_RESULT_CODE, 24)
            putExtra(BORED_ACTIVITY_NAME, "PMK Main")
        })
        finish()
    }

    companion object {
        const val BORED_ACTIVITY_NAME = "BORED_ACTIVITY_NAME"
    }
}

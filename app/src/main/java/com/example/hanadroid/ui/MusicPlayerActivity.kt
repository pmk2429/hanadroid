package com.example.hanadroid.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hanadroid.databinding.ActivityMusicPlayerBinding
import com.example.hanadroid.services.MusicPlayerService
import com.example.hanadroid.util.isServiceRunning

class MusicPlayerActivity : AppCompatActivity() {

    private var _binding: ActivityMusicPlayerBinding? = null
    val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMusicPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val isServiceRunning = isServiceRunning(MusicPlayerService::class.java)
        val musicServiceIntent = Intent(this@MusicPlayerActivity, MusicPlayerService::class.java)

        binding.apply {
            startServiceButton.isEnabled = !isServiceRunning
            stopServiceButton.isEnabled = isServiceRunning

            startServiceButton.setOnClickListener {
                startForegroundService(musicServiceIntent)
                startServiceButton.isEnabled = false
                stopServiceButton.isEnabled = true
            }

            stopServiceButton.setOnClickListener {
                stopService(musicServiceIntent)
                startServiceButton.isEnabled = true
                stopServiceButton.isEnabled = false
            }
        }
    }
}

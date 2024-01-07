package com.example.hanadroid.services// MusicPlayerService.kt

import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import com.example.hanadroid.R
import com.example.hanadroid.ui.MusicPlayerActivity
import com.example.hanadroid.util.createNotificationWithIntentForForegroundService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MusicPlayerService : Service() {

    private val notificationManager: NotificationManager by lazy {
        getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }

    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer.create(this, R.raw.music_file_hana)
        mediaPlayer.isLooping = true
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIFICATION_ID, createNotification())
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                mediaPlayer.start()
            }
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
        mediaPlayer.release()
    }

    private fun createNotification(): Notification {
        return notificationManager.createNotificationWithIntentForForegroundService(
            this@MusicPlayerService,
            MusicPlayerActivity::class.java,
            "Foreground Service Example",
            "Background Music Playing"
        )
    }

    companion object {
        const val NOTIFICATION_ID = 2429
    }
}

package com.example.hanadroid.services// MusicPlayerService.kt

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.hanadroid.ui.MusicPlayerActivity
import com.example.hanadroid.util.createNotificationWithIntentForForegroundService

class MusicPlayerService : Service() {

    private val notificationManager: NotificationManager by lazy {
        getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val foregroundNotification =
            notificationManager.createNotificationWithIntentForForegroundService(
                this,
                MusicPlayerActivity::class.java
            )

        startForeground(1, foregroundNotification)

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}

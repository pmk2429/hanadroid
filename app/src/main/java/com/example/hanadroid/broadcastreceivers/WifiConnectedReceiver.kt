package com.example.hanadroid.broadcastreceivers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.core.app.NotificationCompat
import com.example.hanadroid.R

class WifiConnectedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val connectivityManager =
            context.getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        if (networkCapabilities != null) {
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                // Device is connected to a Wi-Fi network
                showWifiConnectedNotification(context)
            }
        }
    }

    private fun showWifiConnectedNotification(context: Context) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            "wifi_connected_channel",
            "Wi-Fi Connected",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(context, "wifi_connected_channel")
            .setSmallIcon(R.drawable.ic_live_tv_white_24dp)
            .setContentTitle("Wi-Fi Connected")
            .setContentText("Your device is now connected to a Wi-Fi network.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(0, notification)
    }
}
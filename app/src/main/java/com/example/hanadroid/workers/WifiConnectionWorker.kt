package com.example.hanadroid.workers

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class WifiConnectionWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val connectivityManager =
            applicationContext.getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        if (networkCapabilities != null) {
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                // Device is connected to a Wi-Fi network
                showWifiConnectedNotification(applicationContext)
                return Result.success()
            }
        }

        return Result.failure()
    }

    private fun showWifiConnectedNotification(context: Context) {
        // Implement notification logic here (similar to previous BroadcastReceiver example)
    }
}

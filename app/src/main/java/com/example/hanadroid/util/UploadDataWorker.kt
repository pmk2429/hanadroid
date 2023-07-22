package com.example.hanadroid.util

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class UploadDataWorker(
    private val context: Context,
    userParameters: WorkerParameters
) : Worker(context, userParameters) {

    override fun doWork(): Result {
        return try {
            for (i in 0..500) {
                Log.i("pmk", "Sending log $i")
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.sendNotification(context)
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

}

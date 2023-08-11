package com.example.hanadroid.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.example.hanadroid.R

const val WORKER_CHANNEL_ID = "123425"
const val PENDING_INTENT_CHANNEL_ID = "789"

fun NotificationManager.sendNotification(
    context: Context,
    title: String = "Success",
    desc: String = "Worker is completed"
) {
    val NOTIFICATION_ID = 42633687

    val notification = NotificationCompat.Builder(context, WORKER_CHANNEL_ID)
        .setContentTitle(title)
        .setContentText(desc)
        .setSmallIcon(R.drawable.ic_star)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)
        .build()

    notify(NOTIFICATION_ID, notification)
}

fun NotificationManager.launchNotificationWithPendingIntent(
    context: Context,
    activityClass: Class<*>?,
    title: String = "Launch Activity",
    desc: String = "Click here to launch your desired Activity"
) {
    val NOTIFICATION_ID = 2345

    // Create an explicit intent for an Activity in your app
    val intent = Intent(context, activityClass).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    val pendingIntent: PendingIntent =
        PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

    val newMessageNotification = NotificationCompat.Builder(context, PENDING_INTENT_CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_comment)
        .setContentTitle(title)
        .setContentText(desc)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        // Set the intent that will fire when the user taps the notification
        .setContentIntent(pendingIntent)
        // Notice this code calls setAutoCancel(), which automatically removes the notification when the user taps it.
        .setAutoCancel(true)
        .build()

    notify(NOTIFICATION_ID, newMessageNotification)
}

/**
 * Create the NotificationChannel, but only on API 26+ because
 * the NotificationChannel class is new and not in the support library
 */
fun createNotificationChannel(context: Context) {
    val name = context.getString(R.string.channel_name)
    val descriptionText = context.getString(R.string.channel_description)
    val importance = NotificationManager.IMPORTANCE_HIGH
    val channel = NotificationChannel(WORKER_CHANNEL_ID, name, importance).apply {
        description = descriptionText
    }
    // Register the channel with the system
    val notificationManager: NotificationManager =
        context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)
}
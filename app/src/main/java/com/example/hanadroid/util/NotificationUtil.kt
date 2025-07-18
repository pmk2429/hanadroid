package com.example.hanadroid.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.hanadroid.R
import com.example.hanadroid.broadcastreceivers.NotificationIntentReceiver
import com.example.hanadroid.ui.BeerDataActivity
import com.example.hanadroid.ui.DisneyCharactersActivity

const val WORKER_CHANNEL_ID = "123425"
const val PENDING_INTENT_CHANNEL_ID = "789"
const val REMOTE_VIEWS_CHANNEL_ID = "remote_views_channel_id"

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

fun NotificationManager.createNotificationWithIntentForForegroundService(
    context: Context,
    activityClass: Class<*>?,
    title: String = "Music Player",
    desc: String = "Playing Music..."
): Notification {

    // Create Notification with Pending Intent
    val CHANNEL_ID = "ForegroundServiceChannel"
    val serviceChannel = NotificationChannel(
        CHANNEL_ID,
        "Foreground Service Channel",
        NotificationManager.IMPORTANCE_DEFAULT
    )
    createNotificationChannel(serviceChannel)

    // Create Notification with Pending Intent
    val notificationIntent = Intent(context, activityClass)
    val pendingIntent = PendingIntent.getActivity(
        context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE
    )

    return NotificationCompat.Builder(context, CHANNEL_ID)
        .setContentTitle(title)
        .setContentText(desc)
        .setSmallIcon(R.drawable.baseline_music_note_red_a700_24dp)
        .setContentIntent(pendingIntent)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .build()
}

fun isNotificationPermissionGranted(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
        NotificationManagerCompat.from(context).areNotificationsEnabled()
    } else {
        // Notification channels were not introduced before Android O
        true
    }
}

fun NotificationManager.showNotificationWithAcceptAndDeclineActionsPendingIntentBroadcastReceiver(
    context: Context,
    activityClass: Class<*>?
) {
    val NOTIFICATION_ID = 2429

    val serviceChannel = NotificationChannel(
        REMOTE_VIEWS_CHANNEL_ID,
        "Custom Notification Channel",
        NotificationManager.IMPORTANCE_DEFAULT
    )
    createNotificationChannel(serviceChannel)

    val intent = Intent(context, activityClass)
    val pendingIntent =
        PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

    val declineIntent = Intent(context, NotificationIntentReceiver::class.java).apply {
        action = NotificationIntentReceiver.DECLINE_NOTIFICATION_ACTION
    }
    val declinePendingIntent =
        PendingIntent.getBroadcast(context, 0, declineIntent, PendingIntent.FLAG_UPDATE_CURRENT)

    val acceptIntent = Intent(context, NotificationIntentReceiver::class.java).apply {
        action = NotificationIntentReceiver.ACCEPT_NOTIFICATION_ACTION
    }
    val acceptPendingIntent =
        PendingIntent.getBroadcast(context, 0, acceptIntent, PendingIntent.FLAG_UPDATE_CURRENT)

    val remoteViews = RemoteViews(context.packageName, R.layout.notification_layout)
    remoteViews.setTextViewText(R.id.notification_title, "Register for Bounty")
    remoteViews.setTextViewText(R.id.notification_text, "Want to play Bounty Hunter?")
    remoteViews.setImageViewResource(R.id.notification_icon, R.drawable.ic_star)
    remoteViews.setOnClickPendingIntent(R.id.btn_decline, declinePendingIntent)
    remoteViews.setOnClickPendingIntent(R.id.btn_accept, acceptPendingIntent)

    val builder = NotificationCompat.Builder(context, REMOTE_VIEWS_CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_star)
        .setContentIntent(pendingIntent)
        .setCustomContentView(remoteViews)
        .setAutoCancel(true)
        .build()

    notify(NOTIFICATION_ID, builder)
}

fun NotificationManager.showNotificationWithAcceptAndDeclineActionsPendingIntent(
    context: Context,
    activityClass: Class<*>?
) {
    val NOTIFICATION_ID = 242920

    val serviceChannel = NotificationChannel(
        REMOTE_VIEWS_CHANNEL_ID,
        "Custom Notification Channel",
        NotificationManager.IMPORTANCE_DEFAULT
    )
    createNotificationChannel(serviceChannel)

    val declineIntent = Intent(context, DisneyCharactersActivity::class.java)
    val declinePendingIntent =
        PendingIntent.getActivity(context, 0, declineIntent, PendingIntent.FLAG_IMMUTABLE)

    val acceptIntent = Intent(context, BeerDataActivity::class.java)
    val acceptPendingIntent =
        PendingIntent.getActivity(context, 0, acceptIntent, PendingIntent.FLAG_IMMUTABLE)


    val remoteViews = RemoteViews(context.packageName, R.layout.notification_layout)
    remoteViews.setTextViewText(R.id.notification_title, "Register for Bounty")
    remoteViews.setTextViewText(R.id.notification_text, "Want to play Bounty Hunter?")
    remoteViews.setImageViewResource(R.id.notification_icon, R.drawable.ic_star)
    remoteViews.setOnClickPendingIntent(R.id.btn_decline, declinePendingIntent)
    remoteViews.setOnClickPendingIntent(R.id.btn_accept, acceptPendingIntent)

    val notification = NotificationCompat.Builder(context, REMOTE_VIEWS_CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_star)
        .setCustomContentView(remoteViews)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)
        .build()

    notify(NOTIFICATION_ID, notification)
}
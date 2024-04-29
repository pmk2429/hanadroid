package com.example.hanadroid.broadcastreceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.hanadroid.ui.UniversityMainActivity

class NotificationIntentReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        when (intent?.action) {
            DECLINE_NOTIFICATION_ACTION -> {
                // Handle decline action
            }

            ACCEPT_NOTIFICATION_ACTION -> {
                val scheduledIntent = Intent(context, UniversityMainActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(scheduledIntent)
            }
        }
    }

    companion object {
        const val ACCEPT_NOTIFICATION_ACTION = "ACCEPT_ACTION"
        const val DECLINE_NOTIFICATION_ACTION = "DECLINE_ACTION"
    }
}
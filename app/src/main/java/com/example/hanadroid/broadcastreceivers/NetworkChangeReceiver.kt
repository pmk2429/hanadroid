package com.example.hanadroid.broadcastreceivers// NetworkChangeReceiver.kt

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.hanadroid.util.isInternetConnected
import com.example.hanadroid.workers.ScheduleSync

class NetworkChangeReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (isInternetConnected(context)) {
            // Network is connected, trigger the synchronization using SyncWorker
            ScheduleSync(context).scheduleSyncWork()
        }
    }
}

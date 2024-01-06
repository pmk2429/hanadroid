package com.example.hanadroid.broadcastreceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.widget.Toast

class AirplaneModeBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_AIRPLANE_MODE_CHANGED) {
            val isAirplaneModeOn = Settings.System.getInt(
                context.contentResolver,
                Settings.Global.AIRPLANE_MODE_ON,
                0
            ) != 0

            if (isAirplaneModeOn) {
                Toast.makeText(context, "Airplane Mode is turned ON", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Airplane Mode is turned OFF", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

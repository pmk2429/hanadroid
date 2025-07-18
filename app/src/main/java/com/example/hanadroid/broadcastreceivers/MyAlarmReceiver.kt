package com.example.hanadroid.broadcastreceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.hanadroid.ui.DisneyCharactersActivity

class MyAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val scheduledIntent = Intent(context, DisneyCharactersActivity::class.java)
        scheduledIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context?.startActivity(scheduledIntent)
    }
}

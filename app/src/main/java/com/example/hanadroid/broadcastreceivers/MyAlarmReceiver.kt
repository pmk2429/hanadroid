package com.example.hanadroid.broadcastreceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.hanadroid.ui.UniversityMainActivity

class MyAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val scheduledIntent = Intent(context, UniversityMainActivity::class.java)
        scheduledIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context?.startActivity(scheduledIntent)
    }
}

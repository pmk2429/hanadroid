package com.example.hanadroid.broadcastreceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.hanadroid.ui.DisneyCharactersActivity

class HanaBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action.equals(CUSTOM_BROADCAST_ACTION)) {
            Toast.makeText(context, "Broadcast Receiver!", Toast.LENGTH_LONG).show()
            val scheduledIntent = Intent(context, DisneyCharactersActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(scheduledIntent)
        }
    }

    companion object {
        const val CUSTOM_BROADCAST_ACTION = "com.example.hanadroid.MY_CUSTOM_BROADCAST"
    }
}

package com.example.hanadroid.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hanadroid.broadcastreceivers.MyAlarmReceiver
import com.example.hanadroid.databinding.ActivityAlarmTimerBinding
import java.util.Calendar

class AlarmTimerActivity : AppCompatActivity() {

    private var _binding: ActivityAlarmTimerBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAlarmTimerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val alarmIntent = Intent(this, MyAlarmReceiver::class.java)
        val alarmPendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            alarmIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        binding.setAlarmButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = System.currentTimeMillis()
            calendar.set(Calendar.HOUR_OF_DAY, 8)
            calendar.set(Calendar.MINUTE, 0)

            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                alarmPendingIntent
            )

            Toast.makeText(this, "Alarm set for 8:00 AM", Toast.LENGTH_SHORT).show()
        }
    }
}

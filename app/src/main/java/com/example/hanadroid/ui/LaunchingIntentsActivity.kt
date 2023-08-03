package com.example.hanadroid.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.AlarmClock
import android.provider.CalendarContract
import androidx.appcompat.app.AppCompatActivity
import com.example.hanadroid.broadcastreceivers.MyAlarmReceiver
import com.example.hanadroid.databinding.ActivityLaunchingIntentsBinding
import java.time.Instant
import java.util.*

class LaunchingIntentsActivity : AppCompatActivity() {

    private var _binding: ActivityLaunchingIntentsBinding? = null
    val binding get() = _binding!!

    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLaunchingIntentsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bindButtons()
    }

    override fun onResume() {
        super.onResume()
        setupAlarm();
    }

    private fun setupAlarm() {
        // MyAlarmReceiver instance from the system services
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Intent: this is responsible to prepare the android component what PendingIntent will
        // start when the alarm is triggered. That component can be anyone (activity, service, broadcastReceiver, etc)
        // Intent to start the Broadcast Receiver
        val intent = Intent(this, MyAlarmReceiver::class.java)

        // PendingIntent: this is the pending intent, which waits until the right time, to be called by MyAlarmReceiver
        // The Pending Intent to pass in MyAlarmReceiver
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        setAlarm()
    }

    private fun setAlarm() {
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 17)
        }
        calendar.set(Calendar.MINUTE, 49)

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    private fun launchWebView() {
        val webIntent: Intent = Uri.parse("https://www.android.com").let { webpage ->
            Intent(Intent.ACTION_VIEW, webpage)
        }
        startActivity(webIntent)
    }

    private fun launchCalendarToInsert() {
        // Event is on January 23, 2024 -- from 7:30 AM to 10:30 AM.
        val calendarIntent =
            Intent(Intent.ACTION_INSERT, CalendarContract.Events.CONTENT_URI).apply {
                val beginTime: Calendar = Calendar.getInstance().apply {
                    set(2024, 0, 24, 7, 30)
                }
                val endTime = Calendar.getInstance().apply {
                    set(2024, 0, 24, 10, 30)
                }
                putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.timeInMillis)
                putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.timeInMillis)
                putExtra(CalendarContract.Events.TITLE, "PMK Interview")
                putExtra(CalendarContract.Events.EVENT_LOCATION, "Android Everything")
            }
        if (calendarIntent.resolveActivity(packageManager) != null) {
            startActivity(calendarIntent)
        }
    }

    private fun launchCalendarToView() {
        val startMillis: Long = Instant.now().toEpochMilli()
        val builder: Uri.Builder = CalendarContract.CONTENT_URI.buildUpon().appendPath("time")
        ContentUris.appendId(builder, startMillis)
        val intent = Intent(Intent.ACTION_VIEW).setData(builder.build())
        startActivity(intent)
    }

    private fun launchGoogleMaps() {
        val mapIntent: Intent = Uri.parse(
            "geo:0,0?q=1600+Amphitheatre+Parkway,+Mountain+View,+California"
        ).let { location ->
            // Or map point based on latitude/longitude
            // val location: Uri = Uri.parse("geo:37.422219,-122.08364?z=14") // z param is zoom level
            Intent(Intent.ACTION_VIEW, location)
        }
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
    }

    /**
     * Here, the geo represents the lat and long nearby the current location based on GPS.
     * With an added query parameter as restaurants, makes the whole query string.
     *
     * https://developers.google.com/maps/documentation/urls/android-intents
     */
    private fun searchRestaurantsNearby() {
        val gmmIntentUri = Uri.parse("geo:0,0?q=restaurants")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
    }

    /**
     * Here, the geo represents the lat and long in the city of San Francisco.
     * With an added query parameter as restaurants, makes the whole query string.
     *
     * https://developers.google.com/maps/documentation/urls/android-intents
     */
    private fun searchRestaurantsInSanFrancisco() {
        val gmmIntentUri = Uri.parse("geo:37.7749,-122.4194?q=restaurants")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
    }

    private fun launchClockApp() {
        val alarmIntent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
            putExtra(AlarmClock.EXTRA_MESSAGE, "Prep Android")
            putExtra(AlarmClock.EXTRA_HOUR, 6)
            putExtra(AlarmClock.EXTRA_MINUTES, 30)
        }
        startActivity(alarmIntent)
    }

    private fun launchTimer() {
        val clockIntent = Intent(AlarmClock.ACTION_SHOW_TIMERS)
        clockIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(clockIntent);
    }

    private fun ActivityLaunchingIntentsBinding.bindButtons() {
        btnLaunchWebview.setOnClickListener { launchWebView() }
        btnLaunchMaps.setOnClickListener { searchRestaurantsNearby() }
        btnLaunchAlarm.setOnClickListener { launchClockApp() }
        btnLaunchCalendar.setOnClickListener { launchCalendarToView() }
        btnLaunchTimer.setOnClickListener { launchTimer() }
    }
}

package com.example.hanadroid.sharedprefs

import android.content.Context
import android.content.SharedPreferences
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Helper class to create, manage and update SharedPreferences related to Timer.
 */
class TimerDataHelper(context: Context) {

    private var sharedPref: SharedPreferences =
        context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
    private var dateFormat = SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault())

    private var _isTimerRunning = false
    val isTimerRunning get() = _isTimerRunning

    private var _startTime: Date? = null
    val startTime get() = _startTime

    private var _stopTime: Date? = null
    val stopTime get() = _stopTime

    init {
        _isTimerRunning = sharedPref.getBoolean(COUNTING_KEY, false)

        val startString = sharedPref.getString(START_TIME_KEY, null)
        startString?.let { _startTime = dateFormat.parse(it) }
        val stopString = sharedPref.getString(STOP_TIME_KEY, null)
        stopString?.let { _stopTime = dateFormat.parse(it) }
    }

    fun setStartTime(date: Date?) {
        _startTime = date
        with(sharedPref.edit()) {
            val stringDate = date?.let { dateFormat.format(it) }
            putString(START_TIME_KEY, stringDate)
            apply()
        }
    }

    fun setStopTime(date: Date?) {
        _stopTime = date
        with(sharedPref.edit()) {
            val stringDate = date?.let { dateFormat.format(it) }
            putString(STOP_TIME_KEY, stringDate)
            apply()
        }
    }

    fun setTimerCounting(value: Boolean) {
        _isTimerRunning = value
        with(sharedPref.edit()) {
            putBoolean(COUNTING_KEY, value)
            apply()
        }
    }

    companion object {
        const val PREFERENCES = "hana_timer_prefs"
        const val START_TIME_KEY = "startKey"
        const val STOP_TIME_KEY = "stopKey"
        const val COUNTING_KEY = "countingKey"
    }
}

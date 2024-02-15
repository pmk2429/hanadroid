package com.example.hanadroid.util

import android.content.Context
import android.content.SharedPreferences
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TimeUtils {
    class AppInstallationHelper(private val context: Context) {
        private val sharedPreferences: SharedPreferences by lazy {
            context.getSharedPreferences("app_installation", Context.MODE_PRIVATE)
        }

        fun saveInstallationDate() {
            val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            sharedPreferences.edit().putString("installation_date", currentDate).apply()
        }

        fun getDaysSinceInstallation(): Int {
            val installationDateString = sharedPreferences.getString("installation_date", "")
            if (installationDateString.isNullOrEmpty()) {
                return -1 // Installation date not found
            }

            val installationDate =
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(installationDateString)
            val currentDate = Date()
            val diffInMillies = currentDate.time - installationDate.time
            val diffInDays = diffInMillies / (1000 * 60 * 60 * 24)

            return diffInDays.toInt()
        }
    }

}
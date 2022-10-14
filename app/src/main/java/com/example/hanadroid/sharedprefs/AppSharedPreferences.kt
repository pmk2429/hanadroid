package com.example.hanadroid.sharedprefs

import android.content.SharedPreferences

class AppSharedPreferences(
    private val sharedPreferences: SharedPreferences
) {

    fun getAccessToken(): String? = sharedPreferences.getString(ACCESS_TOKEN_KEY, null)

    fun setAccessToken(accessToken: String) {
        sharedPreferences.edit().putString(ACCESS_TOKEN_KEY, accessToken).apply()
    }

    fun getRefreshToken(): String? = sharedPreferences.getString(REFRESH_TOKEN_KEY, null)

    fun setRefreshToken(refreshToken: String) {
        sharedPreferences.edit().putString(REFRESH_TOKEN_KEY, refreshToken).apply()
    }

    companion object {
        const val SHARED_PREFS = "APP_SHARED_PREFS"
        const val ACCESS_TOKEN_KEY = "access_token"
        const val REFRESH_TOKEN_KEY = "access_token"
    }
}
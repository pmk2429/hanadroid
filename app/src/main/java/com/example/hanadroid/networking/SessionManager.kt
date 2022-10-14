package com.example.hanadroid.networking

import com.example.hanadroid.sharedprefs.AppSharedPreferences

class SessionManager constructor(
    private val pref: AppSharedPreferences,
) {

    fun getAccessToken(): String? = pref.getAccessToken()

    fun getRefreshToken(): String? = pref.getRefreshToken()

    fun refreshToken(refreshToken: String): String? = null

    fun logout() {
        pref.setAccessToken("")
    }
}
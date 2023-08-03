package com.example.hanadroid.sharedprefs

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferenceDataStoreConstants {
    val IS_MINOR_KEY = booleanPreferencesKey("IS_MINOR_KEY")
    val AGE_KEY = intPreferencesKey("AGE_KEY")
    val NAME_KEY = stringPreferencesKey("NAME_KEY")
    val MOBILE_NUMBER = longPreferencesKey("MOBILE_NUMBER")
}

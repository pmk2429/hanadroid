package com.example.hanadroid.sharedprefs

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException


// Declaring and Creating the DataStore File for Application
private val Context.dataStore by preferencesDataStore(
    name = "HanaDroidPreferenceDataStore"
)

class PreferenceDataStoreHelper(
    context: Context
) : IPreferenceDataStore {

    private val dataSource = context.dataStore

    /**
     * Returns a flow of data from DataStore.
     * Basically as soon we update the value in Datastore, the values returned by it also changes.
     */
    override suspend fun <T> getPreference(key: Preferences.Key<T>, defaultValue: T): Flow<T> =
        dataSource.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            val result = preferences[key] ?: defaultValue
            result
        }

    override suspend fun <T> getFirstPreference(key: Preferences.Key<T>, defaultValue: T):
            T = dataSource.data.first()[key] ?: defaultValue

    override suspend fun <T> putPreference(key: Preferences.Key<T>, value: T) {
        dataSource.edit { preferences ->
            preferences[key] = value
        }
    }

    override suspend fun <T> removePreference(key: Preferences.Key<T>) {
        dataSource.edit { preferences ->
            preferences.remove(key)
        }
    }

    override suspend fun <T> clearAllPreference() {
        dataSource.edit { preferences ->
            preferences.clear()
        }
    }

}

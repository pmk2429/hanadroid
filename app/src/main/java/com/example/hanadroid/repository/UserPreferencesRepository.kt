package com.example.hanadroid.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

enum class SortOrder {
    NONE,
    BY_UNIVERSITY_TYPE,
    BY_UNIVERSITY_NAME
}

data class UserPreferences(
    val sortOrder: SortOrder
)

/**
 * Saves user preferences in the Preferences DataStore.
 */
class UserPreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    private val TAG: String = "UserPreferencesRepo"

    private object PreferencesKeys {
        val SORT_ORDER = stringPreferencesKey("sort_order")
    }

    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences.", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            mapUserPreferences(preferences)
        }

    /**
     * Enable / disable sort by Employee Type.
     */
    suspend fun enableSortByUniversityName(enable: Boolean) {
        // updateData handles data transactionally, ensuring that if the sort is updated at the same
        // time from another thread, we won't have conflicts
        dataStore.edit { preferences ->
            val currentOrder = SortOrder.valueOf(
                preferences[PreferencesKeys.SORT_ORDER] ?: SortOrder.NONE.name
            )

            val newSortOrder = if (enable) {
                if (currentOrder == SortOrder.NONE) {
                    SortOrder.BY_UNIVERSITY_TYPE
                } else {
                    SortOrder.NONE
                }
            } else {
                if (currentOrder == SortOrder.NONE) {
                    SortOrder.BY_UNIVERSITY_TYPE
                } else {
                    SortOrder.NONE
                }
            }

            preferences[PreferencesKeys.SORT_ORDER] = newSortOrder.name
        }
    }

    suspend fun fetchInitialPreferences() =
        mapUserPreferences(dataStore.data.first().toPreferences())

    private fun mapUserPreferences(preferences: Preferences): UserPreferences {
        // Get the sort order from preferences and convert it to a [SortOrder] object
        val sortOrder =
            SortOrder.valueOf(
                preferences[PreferencesKeys.SORT_ORDER] ?: SortOrder.NONE.name
            )
        return UserPreferences(sortOrder)
    }
}
package com.example.hanadroid.sharedprefs

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.hanadroid.sharedprefs.DisneyCharactersPreferencesRepository.PreferencesKeys.KEY_CHARACTER_SORT_ORDER
import com.example.hanadroid.sharedprefs.DisneyCharactersPreferencesRepository.PreferencesKeys.KEY_FAVORITE_CHARACTERS_IDS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject


enum class SortOrder {
    NONE,
    MOVIES,
    TV_SHOWS,
    MOVIES_AND_SHOWS,
}

data class CharacterPreferences(
    val sortOrder: SortOrder,
    val favoritedIds: List<String>
)

class DisneyCharactersPreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    private val TAG: String = "CharacterPreferencesRepo"

    private object PreferencesKeys {
        val KEY_FAVORITE_CHARACTERS_IDS = stringPreferencesKey("favorite_characters_ids")
        val KEY_CHARACTER_SORT_ORDER = stringPreferencesKey("character_sort_order")
    }

    suspend fun updateFavoritedIds(favoritedIds: List<String>) {
        dataStore.edit { preferences ->
            preferences[KEY_FAVORITE_CHARACTERS_IDS] = favoritedIds.joinToString(",")
        }
    }

    suspend fun sortByMovies(enable: Boolean) {
        dataStore.edit { preferences ->
            val currentSortOrder = SortOrder.valueOf(
                preferences[KEY_CHARACTER_SORT_ORDER] ?: SortOrder.NONE.name
            )
            val newSortOrder =
                if (enable) {
                    if (currentSortOrder == SortOrder.TV_SHOWS) {
                        SortOrder.MOVIES_AND_SHOWS
                    } else {
                        SortOrder.MOVIES
                    }
                } else {
                    if (currentSortOrder == SortOrder.MOVIES_AND_SHOWS) {
                        SortOrder.TV_SHOWS
                    } else {
                        SortOrder.NONE
                    }
                }
            preferences[KEY_CHARACTER_SORT_ORDER] = newSortOrder.name
        }
    }

    suspend fun sortByShows(enable: Boolean) {
        dataStore.edit { preferences ->
            val currentSortOrder = SortOrder.valueOf(
                preferences[KEY_CHARACTER_SORT_ORDER] ?: SortOrder.NONE.name
            )
            val newSortOrder =
                if (enable) {
                    if (currentSortOrder == SortOrder.MOVIES) {
                        SortOrder.MOVIES_AND_SHOWS
                    } else {
                        SortOrder.TV_SHOWS
                    }
                } else {
                    if (currentSortOrder == SortOrder.MOVIES_AND_SHOWS) {
                        SortOrder.MOVIES
                    } else {
                        SortOrder.NONE
                    }
                }
            preferences[KEY_CHARACTER_SORT_ORDER] = newSortOrder.name
        }
    }

    /**
     * Reads th DataStore Preferences and returns the [CharacterPreferences].
     */
    val characterPreferences: Flow<CharacterPreferences> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Cannot read preferences", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            mapUserPreferences(preferences)
        }

    suspend fun fetchInitialPreferences() =
        mapUserPreferences(dataStore.data.first().toPreferences())

    private fun mapUserPreferences(preferences: Preferences): CharacterPreferences {
        // read IDs from the Preferences
        val idString = preferences[KEY_FAVORITE_CHARACTERS_IDS] ?: ""
        val favoritedIds: List<String> = idString.split(",").filter { it.isNotEmpty() }

        // read Sorting order from the Preferences
        val sortOrder = SortOrder.valueOf(
            preferences[KEY_CHARACTER_SORT_ORDER] ?: SortOrder.NONE.name
        )
        return CharacterPreferences(sortOrder, favoritedIds)
    }
}

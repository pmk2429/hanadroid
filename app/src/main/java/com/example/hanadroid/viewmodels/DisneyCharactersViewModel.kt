package com.example.hanadroid.viewmodels

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.hanadroid.model.DisneyCharacter
import com.example.hanadroid.model.uistates.DisneyCharactersUiState
import com.example.hanadroid.networking.NetworkResult
import com.example.hanadroid.repository.DisneyCharactersApiRepository
import com.example.hanadroid.sharedprefs.DisneyCharactersPreferencesRepository
import com.example.hanadroid.sharedprefs.SortOrder
import com.example.hanadroid.util.toDisneyCharacter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DisneyCharactersViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val disneyCharactersApiRepository: DisneyCharactersApiRepository,
    private val disneyCharactersPreferencesRepository: DisneyCharactersPreferencesRepository
) : ViewModel() {

    private var getCharactersJob: Job? = null

    private var _disneyCharactersUiState =
        MutableStateFlow(DisneyCharactersUiState(isLoading = true))
    val disneyCharactersUiState: StateFlow<DisneyCharactersUiState> get() = _disneyCharactersUiState

    private val disneyCharactersPreferences =
        disneyCharactersPreferencesRepository.characterPreferences

    private var _nextPage: Int = 1

    private val _charactersList = MutableStateFlow<List<DisneyCharacter>>(emptyList())
    private var _favoriteCharactersIdList = listOf<String>()

    val initialSetupEvent = liveData {
        emit(disneyCharactersPreferencesRepository.fetchInitialPreferences())
    }

    init {
        viewModelScope.launch {
            combine(
                _charactersList,
                disneyCharactersPreferences
            ) { allCharacters, characterPreferences ->
                val transformedCharacters = filterSortCharacters(
                    allCharacters,
                    characterPreferences.favoritedIds,
                    characterPreferences.sortOrder
                )
                return@combine DisneyCharactersUiState(
                    characters = transformedCharacters,
                    isLoading = false,
                    hasNextPage = false // Initial value, will be updated later
                )
            }.collect {
                _disneyCharactersUiState.value = it
            }
        }
    }

    private fun fetchDisneyCharacters(pageNum: Int = 1) {
        // if the Job is running, cancel the job and reassign it to ViewModelScope
        if (getCharactersJob?.isActive == true) {
            return
        }

        getCharactersJob = viewModelScope.launch {
            val response = disneyCharactersApiRepository.fetchDisneyCharacters(pageNum)
            when (response) {
                is NetworkResult.Success -> {
                    val characters = response.data.characters.map { it.toDisneyCharacter() }
                    // everytime a new list is fetched, keep appending it to list
                    _charactersList.value += characters
                    // handle the nextPage based on the backend response
                    val hasNextPage = !response.data.info.nextPage.isNullOrEmpty()
                    _nextPage = extractPageNum(response.data.info.nextPage)
                    _disneyCharactersUiState.update { currUiState ->
                        currUiState.copy(
                            isLoading = false,
                            hasNextPage = hasNextPage
                        )
                    }
                }

                is NetworkResult.Error -> {
                    _disneyCharactersUiState.update { currUiState ->
                        currUiState.copy(
                            failureMessage = response.message,
                            isLoading = false
                        )
                    }
                }

                is NetworkResult.Exception -> {
                    _disneyCharactersUiState.update { currUiState ->
                        currUiState.copy(
                            failureMessage = response.e.message,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    // run combine operator and get updated value
    private val _disneyCharacterPreferencesFlow = combine(
        _charactersList,
        disneyCharactersPreferences
    ) { allCharacters, characterPreferences ->
        val transformedCharacters = filterSortCharacters(
            allCharacters,
            characterPreferences.favoritedIds,
            characterPreferences.sortOrder
        )
        return@combine DisneyCharactersUiState(
            characters = transformedCharacters,
            isLoading = false
        )
    }

    private fun filterSortCharacters(
        allCharacters: List<DisneyCharacter>,
        favoritedIds: List<String>,
        sortOrder: SortOrder
    ): List<DisneyCharacter> {
        // first get all Characters that are favorited by the user
        allCharacters.forEach { it.isFavorite = favoritedIds.contains(it.id.toString()) }

        // then get all characters filtered by the Sorting order
        return when (sortOrder) {
            SortOrder.NONE -> allCharacters
            SortOrder.MOVIES -> {
                allCharacters.filter { currChar ->
                    currChar.films.isNotEmpty() && currChar.tvShows.isEmpty() && currChar.videoGames.isEmpty()
                }
            }

            SortOrder.TV_SHOWS -> {
                allCharacters.filter { currChar ->
                    currChar.films.isEmpty() && currChar.tvShows.isNotEmpty() && currChar.videoGames.isEmpty()
                }
            }

            SortOrder.MOVIES_AND_SHOWS -> {
                allCharacters.filter { currChar ->
                    currChar.films.isNotEmpty() && currChar.tvShows.isNotEmpty() && currChar.videoGames.isEmpty()
                }
            }
        }
    }

    fun loadMoreData() {
        Log.i("~!@#$", "nextPage: $_nextPage")
        fetchDisneyCharacters(_nextPage)
    }

    fun refreshCharactersData() {
        fetchDisneyCharacters()
    }

    fun enableSortByMovies(enable: Boolean) {
        viewModelScope.launch {
            disneyCharactersPreferencesRepository.sortByMovies(enable)
        }
    }

    fun enableSortByTvShows(enable: Boolean) {
        viewModelScope.launch {
            disneyCharactersPreferencesRepository.sortByShows(enable)
        }
    }

    fun favoriteCharacter(character: DisneyCharacter) {
        // append to the list of Favorited Ids
        _charactersList.value.find { it.id == character.id }?.isFavorite = character.isFavorite
        // save all favorited Characters in to the Preferences
        upsertFavoritedIds()
    }

    private fun upsertFavoritedIds() {
        val idList: List<String> =
            _charactersList.value.filter { it.isFavorite }.map { it.id.toString() }
        _favoriteCharactersIdList = idList
        viewModelScope.launch {
            disneyCharactersPreferencesRepository.updateFavoritedIds(idList)
        }
    }

    private fun extractPageNum(url: String?): Int {
        val queryParams = url?.split("?")?.getOrNull(1)
        return queryParams
            ?.split("&")
            ?.map { it.split("=") }
            ?.find { it[0] == "page" }
            ?.getOrNull(1)
            ?.toInt() ?: -1
    }
}

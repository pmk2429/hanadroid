package com.example.hanadroid.data.disney

import com.example.hanadroid.model.DisneyCharacter
import javax.inject.Inject

class DisneyCharactersApiHelperImpl @Inject constructor(
    private val disneyCharactersApiService: DisneyCharactersApiService
) : DisneyCharactersApiHelper {

    override suspend fun getDisneyCharacters(page: Int, pageSize: Int): List<DisneyCharacter> {
        val response = disneyCharactersApiService.getDisneyCharacters(page, pageSize)
        return response.results
    }
}
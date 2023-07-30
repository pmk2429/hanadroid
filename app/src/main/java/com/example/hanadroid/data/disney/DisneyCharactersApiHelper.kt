package com.example.hanadroid.data.disney

import com.example.hanadroid.model.DisneyCharacter

interface DisneyCharactersApiHelper {

    suspend fun getDisneyCharacters(page: Int, pageSize: Int): List<DisneyCharacter>

}

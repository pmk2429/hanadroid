package com.example.hanadroid.repository

import com.example.hanadroid.data.disney.DisneyCharactersApiHelperImpl
import com.example.hanadroid.model.DisneyCharacter
import com.example.hanadroid.networking.ResponseWrapper
import com.example.hanadroid.util.CoroutineDispatcherProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DisneyCharactersRepository @Inject constructor(
    private val apiHelper: DisneyCharactersApiHelperImpl,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider
) {

    suspend fun fetchDisneyCharacters(
        page: Int,
        pageSize: Int
    ): ResponseWrapper<List<DisneyCharacter>> = withContext(coroutineDispatcherProvider.io()) {
        val result = apiHelper.getDisneyCharacters(page, pageSize)
        try {
            ResponseWrapper.Success(result)
        } catch (e: Exception) {
            ResponseWrapper.Error(e.message!!)
        }
    }

}
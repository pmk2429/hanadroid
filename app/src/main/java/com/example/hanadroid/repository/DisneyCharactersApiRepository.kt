package com.example.hanadroid.repository

import com.example.hanadroid.data.disneyapi.DisneyApiHelper
import com.example.hanadroid.model.DisneyCharactersListResponse
import com.example.hanadroid.networking.NetworkResult
import com.example.hanadroid.util.CoroutineDispatcherProvider
import javax.inject.Inject

class DisneyCharactersApiRepository @Inject constructor(
    private val disneyApiHelper: DisneyApiHelper,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider
) : BaseRepository(coroutineDispatcherProvider) {


    suspend fun fetchDisneyCharacters(
        pageNum: Int,
        pageSize: Int = 20
    ): NetworkResult<DisneyCharactersListResponse> {
        return safeApiCall { disneyApiHelper.getDisneyCharacters(pageNum, pageSize) }
    }

}
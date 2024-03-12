package com.example.hanadroid.data.disneyapi

import com.example.hanadroid.model.DisneyCharactersListResponse
import retrofit2.Response
import javax.inject.Inject

class DisneyApiHelperImpl @Inject constructor(
    private val disneyApiService: DisneyApiService
) : DisneyApiHelper {
    override suspend fun getDisneyCharacters(
        pageNum: Int,
        pageSize: Int
    ): Response<DisneyCharactersListResponse> {
        return disneyApiService.getDisneyCharacters(pageNum, pageSize)
    }
}

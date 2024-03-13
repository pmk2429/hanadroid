package com.example.hanadroid.data.disneyapi

import com.example.hanadroid.model.DisneyCharactersListResponse
import retrofit2.Response

interface DisneyApiHelper {
    suspend fun getDisneyCharacters(
        pageNum: Int,
        pageSize: Int
    ): Response<DisneyCharactersListResponse>
}
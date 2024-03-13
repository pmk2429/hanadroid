package com.example.hanadroid.data.disneyapi

import com.example.hanadroid.model.DisneyCharactersListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Builds the request that calls following url:
 * http://api.disneyapi.dev/character?page=1&pageSize=20
 */
interface DisneyApiService {

    @GET("character")
    suspend fun getDisneyCharacters(
        @Query("page") pageNum: Int,
        @Query("pageSize") pageSize: Int
    ): Response<DisneyCharactersListResponse>
}

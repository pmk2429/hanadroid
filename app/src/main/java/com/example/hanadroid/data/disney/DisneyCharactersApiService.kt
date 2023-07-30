package com.example.hanadroid.data.disney

import com.example.hanadroid.model.DisneyCharacterResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface DisneyCharactersApiService {

    @GET("character")
    suspend fun getDisneyCharacters(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): DisneyCharacterResponse

}

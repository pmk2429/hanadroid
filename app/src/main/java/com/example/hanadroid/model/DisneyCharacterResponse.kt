package com.example.hanadroid.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DisneyCharactersListResponse(
    @Json(name = "info")
    val info: DisneyCharacterResponseInfo,
    @Json(name = "data")
    val characters: List<DisneyCharacterResponse> = emptyList()
)

@JsonClass(generateAdapter = true)
data class DisneyCharacterResponseInfo(
    @Json(name = "count")
    val count: Int,
    @Json(name = "totalPages")
    val totalPages: Int,
    @Json(name = "previousPage")
    val prevPage: String?,
    @Json(name = "nextPage")
    val nextPage: String?
)

/**
 * "_id": 6,
"films": [],
"shortFilms": [],
"tvShows": [],
"videoGames": [],
"parkAttractions": [],
"allies": [],
"enemies": [],
"sourceUrl": "https://disney.fandom.com/wiki/%27Olu_Mel",
"name": "'Olu Mel",
"imageUrl": "https://static.wikia.nocookie.net/disney/images/6/61/Olu_main.png",
"createdAt": "2021-04-12T01:25:09.759Z",
"updatedAt": "2021-12-20T20:39:18.031Z",
"url": "https://api.disneyapi.dev/characters/6",
"__v": 0
 */
@JsonClass(generateAdapter = true)
data class DisneyCharacterResponse(
    @Json(name = "_id")
    val id: Int,
    @Json(name = "films")
    val films: List<String> = emptyList(),
    @Json(name = "shortFilms")
    val shortFilms: List<String> = emptyList(),
    @Json(name = "tvShows")
    val tvShows: List<String> = emptyList(),
    @Json(name = "videoGames")
    val videoGames: List<String> = emptyList(),
    @Json(name = "parkAttractions")
    val parkAttractions: List<String> = emptyList(),
    @Json(name = "sourceUrl")
    val sourceUrl: String = "",
    @Json(name = "name")
    val name: String = "",
    @Json(name = "imageUrl")
    val imageUrl: String = ""
)

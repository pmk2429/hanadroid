package com.example.hanadroid.model

import com.google.gson.annotations.SerializedName

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
data class DisneyCharacter(
    @SerializedName("_id")
    val id: Int,
    @SerializedName("films")
    val films: List<String> = emptyList(),
    @SerializedName("shortFilms")
    val shortFilms: List<String> = emptyList(),
    @SerializedName("tvShows")
    val tvShows: List<String> = emptyList(),
    @SerializedName("videoGames")
    val videoGames: List<String> = emptyList(),
    @SerializedName("parkAttractions")
    val parkAttractions: List<String> = emptyList(),
    @SerializedName("sourceUrl")
    val sourceUrl: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("imageUrl")
    val imageUrl: String = ""
)

package com.example.hanadroid.model

import com.google.gson.annotations.SerializedName

data class CharacterResponse(
    @SerializedName("info")
    val info: Info,
    @SerializedName("results")
    val results: List<RickMortyCharacter> = emptyList()
)

data class Info(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
)

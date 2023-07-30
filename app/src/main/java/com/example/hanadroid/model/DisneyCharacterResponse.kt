package com.example.hanadroid.model

import com.google.gson.annotations.SerializedName

data class DisneyCharacterResponse(
    @SerializedName("info")
    val info: Info,
    @SerializedName("data")
    val results: List<DisneyCharacter> = emptyList()
)

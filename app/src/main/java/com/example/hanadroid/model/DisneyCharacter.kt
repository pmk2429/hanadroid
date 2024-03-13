package com.example.hanadroid.model

data class DisneyCharacter(
    val id: Int,
    val name: String = "",
    val imageUrl: String = "",
    val films: List<String> = emptyList(),
    val shortFilms: List<String> = emptyList(),
    val tvShows: List<String> = emptyList(),
    val videoGames: List<String> = emptyList(),
    val parkAttractions: List<String> = emptyList(),
    val sourceUrl: String = "",
    var isFavorite: Boolean = false
)

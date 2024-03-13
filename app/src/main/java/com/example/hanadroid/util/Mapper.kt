package com.example.hanadroid.util

import com.example.hanadroid.model.DisneyCharacter
import com.example.hanadroid.model.DisneyCharacterResponse
import com.hana.nextdoor.models.ImageResponse
import com.hana.nextdoor.models.Story
import com.hana.nextdoor.models.StoryResponse
import com.hana.nextdoor.models.UiImageSpec

fun StoryResponse.toStory(): Story {
    return Story(
        id = this.id,
        body = this.body,
        author = this.author,
        imageSpec = this.imageSpec.toImageSpec(),
    )
}

fun ImageResponse.toImageSpec(): UiImageSpec {
    return UiImageSpec(
        url = this.url,
        height = this.height,
        width = this.width
    )
}

fun DisneyCharacterResponse.toDisneyCharacter(): DisneyCharacter {
    return DisneyCharacter(
        id = this.id,
        films = this.films,
        shortFilms = this.shortFilms,
        tvShows = this.tvShows,
        videoGames = this.videoGames,
        parkAttractions = this.parkAttractions,
        sourceUrl = this.sourceUrl,
        name = this.name,
        imageUrl = this.imageUrl,
    )
}
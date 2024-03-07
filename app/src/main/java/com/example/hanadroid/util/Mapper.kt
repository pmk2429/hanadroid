package com.example.hanadroid.util

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
package com.hana.nextdoor.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NewsFeedDataResponse(
    @Json(name = "nextPageId")
    val nextPage: String? = null,
    @Json(name = "stories")
    val storiesFeed: List<StoryResponse> = emptyList()
)

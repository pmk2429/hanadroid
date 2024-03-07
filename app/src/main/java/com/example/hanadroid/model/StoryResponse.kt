package com.hana.nextdoor.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * {
 *       "id": String,
 *       "body": String,
 *       "author": String,
 *       "image": {
 *         "url": String,
 *         "width": Int,
 *         "height": Int
 *       }
 *
 */
@JsonClass(generateAdapter = true)
data class StoryResponse(
    @Json(name = "id")
    val id: String,
    @Json(name = "body")
    val body: String,
    @Json(name = "author")
    val author: String,
    @Json(name = "image")
    val imageSpec: ImageResponse
)

@JsonClass(generateAdapter = true)
data class ImageResponse(
    @Json(name = "url")
    val url: String,
    @Json(name = "width")
    val width: Int,
    @Json(name = "height")
    val height: Int,
)

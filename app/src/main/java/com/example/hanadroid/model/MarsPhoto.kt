package com.example.hanadroid.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MarsPhoto(
    @Json(name = "id")
    val id: String,
    @Json(name = "img_src")
    val imgSrcUrl: String
)

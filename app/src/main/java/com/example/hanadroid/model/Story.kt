package com.hana.nextdoor.models

data class Story(
    val id: String,
    val body: String,
    val author: String,
    val imageSpec: UiImageSpec
)

data class UiImageSpec(
    val url: String,
    val width: Int,
    val height: Int,
)

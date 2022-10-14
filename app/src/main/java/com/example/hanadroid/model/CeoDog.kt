package com.example.hanadroid.model

import com.google.gson.annotations.SerializedName

data class CeoDog(
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)

data class WoofDog(
    @SerializedName("url")
    val imageUrl: String,
    @SerializedName("fileSizeBytes")
    val fileSize: Long
)

package com.example.hanadroid.data.model

import com.google.gson.annotations.SerializedName

/**
 * {
 * "activity": "Learn Express.js",
 * "accessibility": 0.25,
 * "type": "education",
 * "participants": 1,
 * "price": 0.1,
 * "link": "https://expressjs.com/",
 * "key": "3943506"
 * }
 */
data class BoredActivity(
    @SerializedName("activity")
    val activity: String = "",
    @SerializedName("accessibility")
    val accessibility: Double = 0.0,
    @SerializedName("type")
    val type: String = "",
    @SerializedName("participants")
    val participants: Int = 0,
    @SerializedName("price")
    val price: Double = 0.0,
    @SerializedName("link")
    val link: String = "",
    @SerializedName("key")
    val key: String = ""
)

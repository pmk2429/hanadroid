package com.example.hanadroid.model

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(tableName = "rick_morty_characters")
data class RickMortyCharacter(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("species")
    val species: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("image")
    val imageUrl: String,
    @SerializedName("location")
    val location: Location
)

data class Location(
    @SerializedName("name")
    val name: String,
    @SerializedName("name")
    val locationUrl: String
)


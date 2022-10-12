package com.example.hanadroid.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "rick_morty_characters")
data class RickMortyCharacter(
    @PrimaryKey
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

@Entity
data class Location(
    @PrimaryKey
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val locationUrl: String
)


package com.example.hanadroid.util

object CountryList {
    private val countriesList = listOf("United States", "France", "India", "Switzerland", "Italy", "Norway")

    const val unitedStates = "United States"

    val randomCountry = (countriesList.indices).random()
}

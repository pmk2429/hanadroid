package com.example.hanadroid.util

object CountryList {
    const val unitedStates = "United States"
    private val countriesList = listOf("United States", "France", "India", "Switzerland", "Italy", "Norway")
    val randomCountry = countriesList.random()
}

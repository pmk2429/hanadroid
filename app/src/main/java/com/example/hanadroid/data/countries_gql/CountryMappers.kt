package com.example.hanadroid.data.countries_gql

import com.example.CountriesQuery
import com.example.CountryQuery.Country
import com.example.hanadroid.model.DetailedCountry
import com.example.hanadroid.model.SimpleCountry

fun Country.toDetailedCountry(): DetailedCountry {
    return DetailedCountry(
        code = code,
        name = name,
        emoji = emoji,
        capital = capital ?: "No capital",
        currency = currency ?: "No currency",
        languages = languages.mapNotNull { it.name },
        continent = continent.name
    )
}

fun CountriesQuery.Country.toSimpleCountry(): SimpleCountry {
    return SimpleCountry(
        code = code,
        name = name,
        emoji = emoji,
        capital = capital ?: "No capital",
    )
}
package com.example.hanadroid.data.countries_gql

import com.example.hanadroid.model.DetailedCountry
import com.example.hanadroid.model.SimpleCountry

interface CountryClient {
    suspend fun getCountries(): List<SimpleCountry>

    suspend fun getCountry(code: String): DetailedCountry?
}
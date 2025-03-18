package com.example.hanadroid.usecases

import com.example.hanadroid.data.countries_gql.CountryClient
import com.example.hanadroid.model.SimpleCountry
import javax.inject.Inject

class GetCountriesUseCase @Inject constructor(
    private val countryClient: CountryClient
) {

    suspend fun execute(): List<SimpleCountry> {
        return countryClient
            .getCountries()
            .sortedBy { it.name }
    }
}

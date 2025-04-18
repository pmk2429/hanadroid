package com.example.hanadroid.usecases

import com.example.hanadroid.data.countries_gql.CountryClient
import com.example.hanadroid.model.DetailedCountry
import javax.inject.Inject

class GetCountryUseCase @Inject constructor(
    private val countryClient: CountryClient
) {

    suspend fun execute(code: String): DetailedCountry? {
        return countryClient.getCountry(code)
    }
}

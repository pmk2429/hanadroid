package com.example.hanadroid.data.countries_gql

import com.apollographql.apollo.ApolloClient
import com.example.CountriesQuery
import com.example.CountryQuery
import com.example.hanadroid.model.DetailedCountry
import com.example.hanadroid.model.SimpleCountry
import javax.inject.Inject

class ApolloCountryClient @Inject constructor(
    private val apolloClient: ApolloClient
) : CountryClient {

    override suspend fun getCountries(): List<SimpleCountry> {
        return apolloClient
            .query(CountriesQuery())
            .execute()
            .data
            ?.countries
            ?.map { it.toSimpleCountry() }
            ?: emptyList()
    }

    override suspend fun getCountry(code: String): DetailedCountry? {
        return apolloClient
            .query(CountryQuery(country_code = code))
            .execute()
            .data
            ?.country
            ?.toDetailedCountry()
    }
}

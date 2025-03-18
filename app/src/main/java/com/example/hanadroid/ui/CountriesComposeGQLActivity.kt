package com.example.hanadroid.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.hanadroid.ui.composables.CountriesScreen
import com.example.hanadroid.viewmodels.CountriesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CountriesComposeGQLActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val viewModel: CountriesViewModel by viewModels()
                val countriesUiState by viewModel.countriesUiState.collectAsState()
                CountriesScreen(
                    state = countriesUiState,
                    onSelectCountry = viewModel::selectCountry,
                    onDismissCountryDialog = viewModel::dismissCountryDialog
                )
            }
        }
    }
}

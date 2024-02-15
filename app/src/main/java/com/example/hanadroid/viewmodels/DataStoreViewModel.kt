package com.example.hanadroid.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hanadroid.sharedprefs.PreferenceDataStoreConstants.AGE_KEY
import com.example.hanadroid.sharedprefs.PreferenceDataStoreConstants.NAME_KEY
import com.example.hanadroid.sharedprefs.PreferenceDataStoreHelper
import kotlinx.coroutines.launch

class DataStoreViewModel(
    private val preferenceDataStoreHelper: PreferenceDataStoreHelper
) : ViewModel() {

    private var _name: String? = null
    val name get() = _name!!

    init {
        initPreferenceDataStore()
    }

    private fun initPreferenceDataStore() {
        viewModelScope.launch {
            preferenceDataStoreHelper.getPreference(NAME_KEY, "").collect {
                _name = it
            }
        }

        viewModelScope.launch {
            val name = preferenceDataStoreHelper.getFirstPreference(NAME_KEY, "")
        }
    }

    fun setPreference(value: String) {
        viewModelScope.launch {
            preferenceDataStoreHelper.putPreference(NAME_KEY, value)
            preferenceDataStoreHelper.putPreference(AGE_KEY, 1)
            preferenceDataStoreHelper.putPreference(NAME_KEY, value)
        }
    }

}

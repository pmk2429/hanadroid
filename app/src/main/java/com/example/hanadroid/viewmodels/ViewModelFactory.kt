package com.example.hanadroid.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hanadroid.repository.NonHiltItemRepository

class ViewModelFactory(
    private val repository: NonHiltItemRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NonHiltViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NonHiltViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

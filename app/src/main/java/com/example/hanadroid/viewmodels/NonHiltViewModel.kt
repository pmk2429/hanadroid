package com.example.hanadroid.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hanadroid.model.SampleItem
import com.example.hanadroid.repository.NonHiltItemRepository

class NonHiltViewModel(val repository: NonHiltItemRepository) : ViewModel() {

    private var _items: MutableLiveData<List<SampleItem>> = MutableLiveData(repository.getItems())
    val items: LiveData<List<SampleItem>> get() = _items
}

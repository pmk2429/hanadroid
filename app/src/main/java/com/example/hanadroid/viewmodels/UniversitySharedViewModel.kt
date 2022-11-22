package com.example.hanadroid.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hanadroid.model.University

class UniversitySharedViewModel : ViewModel() {
    private var _universityLiveData : MutableLiveData<University> = MutableLiveData()
    val universityLiveData : LiveData<University> = _universityLiveData

    fun updateUniversity(university: University) {
        _universityLiveData.postValue(university)
    }

}

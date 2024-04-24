package com.example.hanadroid.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hanadroid.util.InteractionQueue
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InteractionViewModel @Inject constructor() : ViewModel() {
    private val interactionQueue = InteractionQueue<Any>()

    private val _interactionLiveData = MutableLiveData<Any>()
    val interactionLiveData: LiveData<Any> = _interactionLiveData

    private val formData = mutableMapOf<String, Any>()

    fun enqueueInteraction(interaction: Any) {
        interactionQueue.enqueue(interaction)
        processNextInteraction()
    }

    private fun processNextInteraction() {
        if (!interactionQueue.isEmpty()) {
            _interactionLiveData.postValue(interactionQueue.dequeue())
        }
    }

    fun addFormData(key: String, value: Any) {
        formData[key] = value
    }

    fun getFormData(): Map<String, Any> {
        return formData
    }

    fun clearFormData() {
        formData.clear()
    }
}

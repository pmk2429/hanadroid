package com.example.hanadroid.ui.uistate

import com.example.hanadroid.model.MarsPhoto

data class MarsDataUiState(
    val marsDataList: List<MarsPhoto> = emptyList(),
    val failureMessage: String? = null,
    val isLoading: Boolean = false
)

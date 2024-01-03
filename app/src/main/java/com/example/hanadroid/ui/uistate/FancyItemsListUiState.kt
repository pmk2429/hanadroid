package com.example.hanadroid.ui.uistate

import com.example.hanadroid.model.FancyModel

data class FancyItemsListUiState(
    val fancyItems: List<FancyModel> = emptyList(),
    val isLoading: Boolean = false,
    val isItemAdded: Boolean = false,
    val isItemRemoved: Boolean = false,
    val itemRemovedAtIndex: Int = 0
)

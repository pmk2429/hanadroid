package com.example.hanadroid.viewmodels

import androidx.lifecycle.ViewModel
import com.example.hanadroid.ui.uistate.FancyItemsListUiState
import com.example.hanadroid.util.CreateFancyModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class FancyListViewModel @Inject constructor() : ViewModel() {

    private var _fancyItemsList: MutableStateFlow<FancyItemsListUiState> =
        MutableStateFlow(FancyItemsListUiState(fancyItems = CreateFancyModel.fancyItems))
    val fancyItems get() = _fancyItemsList

    fun addRandomFancyItem() {
        val fancyItem = CreateFancyModel.randomFancyItem()
        _fancyItemsList.update { currentUiState ->
            val newList = currentUiState.fancyItems.toMutableList()
            newList.add(fancyItem)
            currentUiState.copy(fancyItems = newList, isItemAdded = true)
        }
    }
}

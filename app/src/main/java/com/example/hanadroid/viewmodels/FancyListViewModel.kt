package com.example.hanadroid.viewmodels

import android.util.Log
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

    // TODO: Add logic to avoid duplicate and sort all items using Toggle in UI
    fun addRandomFancyItem() {
        val fancyItem = CreateFancyModel.randomFancyItem()
        _fancyItemsList.update { currentUiState ->
            val newList = currentUiState.fancyItems.toMutableList()
            newList.add(fancyItem)
            currentUiState.copy(fancyItems = newList, isItemAdded = true)
        }
    }

    fun removeFancyItem() {
        _fancyItemsList.update { currentUiState ->
            val updatedList = currentUiState.fancyItems.toMutableList()
            val randomIndex = (0 until updatedList.size).random()
            Log.i("~!@#$", "removeFancyItem Index: $randomIndex")
            updatedList.removeAt(randomIndex)
            currentUiState.copy(
                fancyItems = updatedList,
                isItemRemoved = true,
                itemRemovedAtIndex = randomIndex
            )
        }
    }

    fun handleFancyItemClick() {
        Log.i("~!@#$", "handleFancyItemClick: ")
    }
}

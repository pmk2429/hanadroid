package com.example.hanadroid.ui.uistate

import com.example.hanadroid.model.University
import com.example.hanadroid.sharedprefs.SortOrder
import com.example.hanadroid.sharedprefs.UniversitySortOrder

/**
 * Separate State holders for rendering Data, Error, Empty or Loading UI states to avoid doing
 * logic deduction in the View (Fragment/Activity) layer.
 */
sealed class UniversityListUiState {

    object LoadingState : UniversityListUiState()

    data class ListState(
        val universitiesList: List<University> = emptyList(),
        val universitySortOrder: UniversitySortOrder
    ) : UniversityListUiState()

    data class ErrorState(
        val failureMessage: String? = null
    ) : UniversityListUiState()

    object EmptyListState : UniversityListUiState()

}

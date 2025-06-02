package com.example.hanadroid.model.uistates

import android.view.View
import androidx.paging.CombinedLoadStates
import androidx.paging.PagingData
import com.example.hanadroid.model.BeerInfo
import com.example.hanadroid.model.FancyModel
import com.example.hanadroid.viewmodels.infra.BaseEvent
import com.example.hanadroid.viewmodels.infra.BaseResult
import com.example.hanadroid.viewmodels.infra.BaseViewAction
import com.example.hanadroid.viewmodels.infra.BaseViewState

data class ListViewState(
    val page: PagingData<BeerInfo>? = null,
    val adapterList: List<BeerInfo> = emptyList(),
    val errorMessageResource: Int? = null,
    val errorMessage: String? = null,
    val loadingStateVisibility: Int? = View.GONE,
    val errorVisibility: Int? = View.GONE
) : BaseViewState

sealed class BaseViewEffect : BaseViewAction {
    data class TransitionToScreen(val url: String) : BaseViewEffect()
}

sealed class Event : BaseEvent {
    object SwipeToRefreshEvent : Event()
    data class LoadState(val state: CombinedLoadStates) : Event()
    data class ListItemClicked(val item: BeerInfo) : Event()

    // Suspended
    object ScreenLoad : Event()
}

sealed class Result : BaseResult {
    data class Error(val errorMessage: String?) : Result()
    data class Content(val content: FancyModel) : Result()
    //data class ItemClickedResult(val item: Photo, val sharedElement: View) : Result()
}

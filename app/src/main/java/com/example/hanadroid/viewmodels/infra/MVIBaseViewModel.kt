package com.example.hanadroid.viewmodels.infra

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.hanadroid.networking.ResponseWrapper

abstract class BaseViewModel<ViewState : BaseViewState,
        ViewAction : BaseViewAction,
        Event : BaseEvent,
        Result : BaseResult>(
    initialState: ViewState
) : ViewModel() {

    suspend fun onSuspendedEvent(event: Event) {
        Log.d("~!@#$", "----- suspend event ${event.javaClass.simpleName}")
        suspendEventToResult(event)
    }

    abstract fun eventToResult(event: Event)

    abstract suspend fun suspendEventToResult(event: Event)

    abstract fun resultToViewState(result: ResponseWrapper<Result>)

    abstract fun resultToViewAction(result: ResponseWrapper<Result>)

}

interface BaseViewState

interface BaseViewAction

interface BaseEvent

interface BaseResult

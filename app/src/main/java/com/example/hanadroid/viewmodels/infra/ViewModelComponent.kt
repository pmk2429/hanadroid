package com.example.hanadroid.viewmodels.infra

import com.example.hanadroid.compose.vieweffects.ViewEffect
import com.example.hanadroid.compose.vieweffects.ViewEffectProducer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Used in conjunction with the [CompositeViewModel]. Used to handle aspects of UI that are shared
 * across multiple screens
 */
abstract class ViewModelComponent<Action, State>(initialState: State) : ViewEffectProducer {

    protected val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<State> = _state

    private val viewEffects = MutableSharedFlow<ViewEffect>()

    protected fun updateState(update: State.() -> State) {
        _state.update(update)
    }

    protected fun withState(action: (State) -> Unit) {
        action(state.value)
    }

    protected fun triggerViewEffect(viewEffect: ViewEffect) {
        scope.launch { viewEffects.emit(viewEffect) }
    }

    abstract fun handleAction(action: Action)

    override fun viewEffects(): Flow<ViewEffect> = viewEffects

    open fun onCleared() {
        scope.cancel()
    }
}
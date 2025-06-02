//package com.example.hanadroid.viewmodels.infra
//
//import com.airbnb.mvrx.MavericksState
//import com.example.hanadroid.compose.vieweffects.ViewEffect
//import com.example.hanadroid.viewmodels.infra.ViewModelComponent
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.MutableSharedFlow
//import kotlinx.coroutines.launch
//
//// 1. First, create the CompositeViewModel base class
//open class CompositeViewModel<S : MavericksState>(initialState: S), ViewEffectProducer {
//
//    private val components = mutableListOf<Any>()
//    private val _viewEffects = MutableSharedFlow<ViewEffect>()
//
//    override fun viewEffects(): Flow<ViewEffect> = _viewEffects
//
//    // Method to attach a ViewModelComponent
//    protected fun <A, CS> attachViewModelComponent(
//        component: ViewModelComponent<A, CS>,
//        stateReducer: S.(CS) -> S
//    ) {
//        components.add(component)
//
//        // Observe component's state and update parent state
//        viewModelScope.launch {
//            component.state.collect { componentState ->
//                setState { stateReducer(componentState) }
//            }
//        }
//
//        // Forward component's view effects to parent
//        viewModelScope.launch {
//            component.viewEffects().collect { viewEffect ->
//                _viewEffects.emit(viewEffect)
//            }
//        }
//    }
//
//    // Method to get a component by type
//    @Suppress("UNCHECKED_CAST")
//    fun <T : Any> getComponent(clazz: Class<T>): T {
//        return components.find { clazz.isAssignableFrom(it::class.java) } as? T
//            ?: throw IllegalStateException("Component ${clazz.simpleName} not found")
//    }
//
//    // Convenience method with reified type
//    inline fun <reified T : Any> getComponent(): T = getComponent(T::class.java)
//
//    override fun onCleared() {
//        super.onCleared()
//        components.filterIsInstance<ViewModelComponent<*, *>>().forEach { it.onCleared() }
//        components.clear()
//    }
//}
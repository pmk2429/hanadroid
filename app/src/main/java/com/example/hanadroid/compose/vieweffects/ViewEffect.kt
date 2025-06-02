package com.example.hanadroid.compose.vieweffects

import android.content.Context
import android.view.View
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

/** Internal holder class for View Effects. Should not be accessed directly.
 *  This is a custom implementation for handling side effects in Android,
 *  specifically designed to bridge the gap between your business logic
 *  (usually in ViewModels) and UI effects that should happen only once
 *  (like showing a toast, navigation, etc.).
 *
 *  This pattern is particularly useful for transient UI effects that should not be
 *  part of your UI state, such as:
 * - Navigation events
 * - Toasts and snackbars
 * - Dialog showing
 * - Animation triggers
 * - Permission requests
 */
data class ViewEffectContext(
    val context: Context,
    val localView: View,
    val coroutineScope: CoroutineScope,
    val extraData: MutableMap<String, Any?> = mutableMapOf(),
    val viewEffectMap:
    MutableMap<KClass<out ViewEffect>, suspend ViewEffectContext.(effect: ViewEffect) -> Unit> =
        mutableMapOf(),
) {
    private val _viewEffectsFlow: MutableSharedFlow<ViewEffect> = MutableSharedFlow()
    val viewEffectFlow: Flow<ViewEffect> = _viewEffectsFlow

    fun triggerViewEffect(viewEffect: ViewEffect) {
        coroutineScope.launch { _viewEffectsFlow.emit(viewEffect) }
    }
}

/** Base ViewEffect interface. Implement this to create a ViewEffect */
interface ViewEffect

/** Used by [SetupViewEffects] to provide ViewEffects. This is setup inside the [BaseViewModel] */
interface ViewEffectProducer {
    fun viewEffects(): Flow<ViewEffect>
}
package com.example.hanadroid.compose.vieweffects

sealed interface MyFeatureViewEffects : ViewEffect {
    data object ErrorToast : MyFeatureViewEffects
    data class UndoDeleteToast(val onUndo: () -> Unit) : MyFeatureViewEffects
}

// or

data object ErrorToast : ViewEffect
data class UndoDeleteToast(val onUndo: () -> Unit) : ViewEffect

/*
SetupViewEffectHandlers(
viewEffects = viewModel.viewEffects(),
onSetupViewEffect = {
    registerEffect<ErrorToast> {
        showToastError(stringResource(R.string.error_message))
    }
    registerEffect<UndoDeleteToast> { undoDeleteToast ->
        showToast(
            text = stringResource(R.string.delete_message),
            action = ToastAction(
                text = stringResource(R.string.undo),
                onClick = { undoDeleteToast.undo() }
            )
        )
    }
}
) {
    // wrap content
}
*/

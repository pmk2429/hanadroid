package com.example.hanadroid.util

import android.view.View
import android.view.ViewGroup
import androidx.core.view.allViews
import androidx.core.view.children
import com.example.hanadroid.ui.views.SingleRowView

inline fun <reified T : View> ViewGroup.allViewsOfType(): List<View> {
    val views = mutableListOf<View>()
    views.addAll((0 until childCount).map(this::getChildAt))
    views.filterIsInstance<SingleRowView>()
    return views
}


inline fun <reified T : View> ViewGroup.allDirectChildren(): List<View> {
    val views = mutableListOf<View>()
    views.addAll((0 until childCount).map(this::getChildAt))
    return views
}

inline fun <reified T : View> View.allChildViews(): List<View> {
    if (this !is ViewGroup || childCount == 0) return listOf(this)

    return children
        .toList()
        .flatMap { it.allViews }
        .plus(this as View)
}

/**
 * Extension function sample by passing in Lambda Function Syntax:
 * val lambdaName : () -> ReturnType = { args1, args2, ... -> codeBody }
 *
 * Lambda sample:
 * predicate: (Char) -> Boolean
 * Takes in a Character argument and returns a Boolean
 */
inline fun CharSequence.findFirstIndex(predicate: (Char) -> Boolean): Int {
    for (index in indices) {
        if (predicate(this[index])) {
            return index
        }
    }
    return -1
}

inline fun CharSequence.findLastIndex(predicate: (Char) -> Boolean): Int {
    for (index in indices.reversed()) {
        if (predicate(this[index])) {
            return index
        }
    }
    return -1
}

package com.example.hanadroid.util

import android.view.View
import android.view.ViewGroup
import androidx.core.view.allViews
import androidx.core.view.children
import com.example.hanadroid.model.AppError

fun <T : View> ViewGroup.getViewsByType(viewTypeClass: Class<T>): List<T> {
    return mutableListOf<T?>().apply {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            (child as? ViewGroup)?.let {
                addAll(child.getViewsByType(viewTypeClass))
            }
            if (viewTypeClass.isInstance(child)) {
                add(viewTypeClass.cast(child))
            }
        }
    }.filterNotNull()
}

fun <T : View> View.allDirectChildren(): List<View> {
    if (this !is ViewGroup || childCount == 0) return listOf(this)

    return mutableListOf<View>().apply {
        addAll((0 until childCount).map { i -> getChildAt(i) })
    }.toList()
}

inline fun <reified T : View> View.allChildViews(): List<View> {
    // if current View is not ViewGroup, it won't have any children
    // hence return empty List
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

fun Throwable?.toAppError(): AppError {
    return AppError(message = this?.message ?: "NULL", cause = this?.cause)
}

// with duplicate
inline fun <T, R> pmkWith(receiver: T, block: T.() -> R): R {
    return receiver.block()
}

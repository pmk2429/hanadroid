package com.example.hanadroid.util

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * Extension to call to launch Coroutine with LifecycleScope when it's atleast STARTED from Fragment.
 */
fun <T> AppCompatActivity.launchAndRepeatWithLifecycleOwner(
    flow: Flow<T?>,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    block: (T) -> Unit
) {
    lifecycleScope.launch {
        flow.flowWithLifecycle(this@launchAndRepeatWithLifecycleOwner.lifecycle, minActiveState)
            .collect {
                it?.let(block)
            }
    }
}

fun AppCompatActivity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun AppCompatActivity.popBackStack() {
    hideKeyboard()
    supportFragmentManager.popBackStack()
}

fun AppCompatActivity.popBackStackInclusive() {
    hideKeyboard()
    if (supportFragmentManager.backStackEntryCount > 0)
        supportFragmentManager.popBackStack(
            supportFragmentManager.getBackStackEntryAt(0).id,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
}

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commit()
}


fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction { add(frameId, fragment, fragment.javaClass.simpleName) }
}

fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction {
        replace(
            frameId,
            fragment,
            fragment.javaClass.simpleName
        )
    }
}

fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int, addToStack: Boolean) {
    supportFragmentManager.inTransaction {
        if (addToStack) {
            replace(frameId, fragment, fragment.javaClass.simpleName)
                .addToBackStack(fragment.javaClass.simpleName)
        } else {
            replace(frameId, fragment, fragment.javaClass.simpleName)
        }
    }
}

fun AppCompatActivity.replaceFragment(
    fragment: Fragment,
    frameId: Int,
    addToStack: Boolean,
    clearBackStack: Boolean
) {
    supportFragmentManager.inTransaction {
        if (clearBackStack && supportFragmentManager.backStackEntryCount > 0) {
            val first = supportFragmentManager.getBackStackEntryAt(0)
            supportFragmentManager.popBackStack(first.id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        if (addToStack) {
            replace(frameId, fragment, fragment.javaClass.simpleName)
                .addToBackStack(fragment.javaClass.simpleName)
        } else {
            replace(frameId, fragment, fragment.javaClass.simpleName)
        }
    }
}

fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int, addToStack: Boolean) {
    supportFragmentManager.inTransaction {
        if (addToStack) {
            add(frameId, fragment, fragment.javaClass.simpleName)
                .addToBackStack(fragment.javaClass.simpleName)
        } else {
            add(frameId, fragment)
        }
    }
}


fun AppCompatActivity.getCurrentFragment(): Fragment? {
    val fragmentManager = supportFragmentManager
    var fragmentTag: String? = ""

    if (fragmentManager.backStackEntryCount > 0)
        fragmentTag =
            fragmentManager.getBackStackEntryAt(fragmentManager.backStackEntryCount - 1).name

    return fragmentManager.findFragmentByTag(fragmentTag)
}

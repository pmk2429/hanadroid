package com.example.hanadroid.util

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.core.view.allViews
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.hanadroid.model.AppError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

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

inline fun <reified T : View> View.allDirectChildren(): List<View> {
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
 * `debounceInterval` specifies the debounce interval in milliseconds (e.g., 2 seconds).
 * The Handler is used to delay setting isClickable back to true until after the debounce
 * interval has elapsed. This prevents rapid consecutive button clicks within the debounce
 * interval from triggering the action multiple times.
 */
fun Button.setDebouncedOnClickListener(debounceInterval: Long = 2000L, onClick: (Button) -> Unit) {
    val handler = Handler(Looper.getMainLooper())
    var isClickable = true

    setOnClickListener {
        if (isClickable) {
            isClickable = false
            handler.postDelayed({
                isClickable = true
            }, debounceInterval)

            onClick(this)
        }
    }
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

/**
 * The inline function accepts a receiver and a block that executes on the receiver.
 * The return of this function is same as the result of Lambda (block) after execution.
 */
inline fun <T, R> pmkWith(receiver: T, block: T.() -> R): R {
    return receiver.block()
}

/**
 * Delays on Views
 *
 * Uses safe check to locate the LifecycleOwner responsible for managing this View, if present by
 * calling [findViewTreeLifecycleOwner()].
 * The block of code executed when calling this method will be delayed by the amount of explicit
 * input duration in milliseconds. That way the code block is suspended using the Main dispatcher
 * since we want to interact with the View.
 */
fun View.delayOnLifecycle(
    durationInMillis: Long,
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    block: () -> Unit,
): Job? = findViewTreeLifecycleOwner()?.let { lifecycleOwner ->
    lifecycleOwner.lifecycle.coroutineScope.launch(dispatcher) {
        delay(timeMillis = durationInMillis)
        block() // execute the code block which is wrapped in delayOnLifecycle function call
    }
}

/**
 * Extension to call to launch Coroutine with LifecycleScope when it's atleast STARTED from Fragment.
 */
fun <T> Fragment.launchAndRepeatWithLifecycleOwner(
    flow: Flow<T?>,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    block: (T) -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        flow.flowWithLifecycle(
            this@launchAndRepeatWithLifecycleOwner.viewLifecycleOwner.lifecycle,
            minActiveState
        ).collect {
            it?.let(block)
        }
    }
}

/**
 * This gives you the touch event coordinates relative to the view that has the touch listener
 * assigned to it. The top left corner of the view is (0, 0).
 * If you move your finger above the view, then y will be negative.
 * If you move your finger left of the view, then x will be negative.
 */
@SuppressLint("ClickableViewAccessibility")
val handleTouchListener = View.OnTouchListener { _, event ->
    val x = event.x.toInt()
    val y = event.y.toInt()

    val xRelToScreen = event.rawX.toInt()
    val yRelToScreen = event.rawY.toInt()

    when (event.action) {
        MotionEvent.ACTION_DOWN -> Log.i("~!@#", "touched down ($x, $y)")
        MotionEvent.ACTION_MOVE -> Log.i("~!@#", "moving: ($x, $y)")
        MotionEvent.ACTION_UP -> Log.i("~!@#", "touched up ($x, $y)")
    }
    false
}

@Suppress("DEPRECATION")
fun Context.getPackageInfo(): PackageInfo {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
    } else {
        packageManager.getPackageInfo(packageName, 0)
    }
}

fun isInternetConnected(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val network = connectivityManager.activeNetwork
    val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
    return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

@Suppress("DEPRECATION") // Deprecated for third party Services.
fun <T> Context.isServiceRunning(service: Class<T>) =
    (getSystemService(ACTIVITY_SERVICE) as ActivityManager)
        .getRunningServices(Integer.MAX_VALUE)
        .any { it.service.className == service.name }


fun View.findViewById(id: Int): View? {
    return if (id == View.NO_ID) {
        null
    } else findViewTraversal(id)

    // Traverse the view hierarchy to find the view with the given id
}

private fun View.findViewTraversal(id: Int): View? {
    if (id == this.id) {
        return this
    }
    if (this is ViewGroup) {
        val parent = this as ViewGroup
        val count = parent.childCount
        for (i in 0 until count) {
            val child = parent.getChildAt(i)
            val view: View? = child.findViewTraversal(id)
            if (view != null) {
                return view
            }
        }
    }
    return null
}

package com.example.hanadroid.util

import android.util.Log
import kotlinx.coroutines.*

class CoroutineCountdownTimer {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Default + job)

    private fun startCoroutineTimer(
        delayMillis: Long = 0,
        repeatMillis: Long = 0,
        action: () -> Unit
    ) = scope.launch(Dispatchers.IO) {
        delay(delayMillis)
        if (repeatMillis > 0) {
            while (true) {
                action()
                delay(repeatMillis)
            }
        } else {
            action()
        }
    }

    private val timer: Job = startCoroutineTimer(delayMillis = 0, repeatMillis = 20000) {
        Log.i("~!@#", "Background - tick")
        // doSomethingBackground()
        scope.launch(Dispatchers.Main) {
            Log.i("~!@#", "Main thread - tick")
            // doSomethingMainThread()
        }
    }

    fun startTimer() {
        timer.start()
    }

    fun cancelTimer() {
        timer.cancel()
    }
}

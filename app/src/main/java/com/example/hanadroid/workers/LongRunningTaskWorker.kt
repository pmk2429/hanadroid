package com.example.hanadroid.workers

import android.content.Context
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters

/**
 * The intention of LongRunningTaskWorker is to simulate a long-running task
 * by introducing small pauses (sleeps) between iterations. By dividing the total duration of
 * the task by 100 and sleeping for that amount of time, the worker effectively introduces delays
 * that represent smaller steps within the overall task.
 */
class LongRunningTaskWorker(
    context: Context,
    userParameters: WorkerParameters
) : Worker(context, userParameters) {
    override fun doWork(): Result {
        val inputData = inputData
        val duration = inputData.getLong(KEY_DURATION, DEFAULT_DURATION)

        for (i in 1..100) {
            // For example, if duration is set to 10000 milliseconds (10 seconds),
            // then Thread.sleep(duration / 100) will sleep for 100 milliseconds between
            // each iteration.
            // This gives the appearance of progress updates occurring every 100 milliseconds
            // as the task simulates its progress.
            Thread.sleep(duration / 100) // pretend to be performing long running task
            setProgressAsync(Data.Builder().putInt(KEY_PROGRESS, i).build()) // update worker
        }

        return Result.success()
    }

    companion object {
        const val KEY_DURATION = "key_duration"
        const val DEFAULT_DURATION = 5000L // 5 Seconds if not supplied
        const val KEY_PROGRESS = "key_progress"
        const val WORKER_TAG = "long_running_worker_tag"
    }
}

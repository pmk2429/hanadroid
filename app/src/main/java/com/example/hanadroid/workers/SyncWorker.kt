package com.example.hanadroid.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.runBlocking
import java.io.IOException
import kotlin.math.pow

class SyncWorker(
    val context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    private val MAX_RETRY_COUNT = 3

    override fun doWork(): Result {
        // TODO get access to repository

        val dataToSync = runBlocking {
            // TODO call the coroutine to get data from DAO using repository
        }

        if (true) {
            try {
                // get response from backend API
            } catch (e: IOException) {
                // Handle network error
                if (runAttemptCount < MAX_RETRY_COUNT) {
                    // Retry the work with exponential backoff
                    val nextDelay = calculateExponentialBackoff(runAttemptCount)
                    return Result.retry()
                } else {
                    // Max retry count reached, stop retrying
                    return Result.failure()
                }
            }
        }

        return Result.success()
    }

    private fun calculateExponentialBackoff(attempt: Int): Long {
        val baseDelay = 2 // Initial delay in seconds
        return baseDelay * 2.0.pow(attempt.toDouble()).toLong() * 1000
    }
}

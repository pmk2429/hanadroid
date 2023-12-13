package com.example.hanadroid.workers

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters

class UploadDataWorker(
    context: Context,
    userParameters: WorkerParameters
) : Worker(context, userParameters) {

    override fun doWork(): Result {
        return try {
            for (i in 0..500) {
                Log.i("pmk", "Sending log $i")
                setProgressAsync(Data.Builder().putInt(KEY_UPLOAD_WORKER_PROGRESS, i).build())
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    companion object {
        const val TAG_WORKER_LOG = "worker_log"
        const val KEY_UPLOAD_WORKER_PROGRESS = "KEY_UPLOAD_WORKER_PROGRESS"
    }

}

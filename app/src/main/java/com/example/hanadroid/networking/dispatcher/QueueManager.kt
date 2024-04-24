package com.example.hanadroid.networking.dispatcher

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Handler
import android.os.Looper
import com.example.hanadroid.util.CoroutineDispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.ArrayDeque
import java.util.Queue
import javax.inject.Inject

class QueueManager @Inject constructor(
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider
) {
    /**
     * A Double Ended Queue to add operations to the Dispatcher.
     */
    private val taskQueue: Queue<suspend () -> Unit> = ArrayDeque()

    /**
     * Lock to check the current status of the Request already processed or not.
     */
    private var isProcessing = false

    private val mainHandler = Handler(Looper.getMainLooper())

    private fun isConnectedToWifi(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(network) ?: return false
        return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
    }

    fun enqueueTask(task: () -> Unit) {
        taskQueue.offer(task)
        if (!isProcessing) {
            processQueue()
        }
    }

    /**
     * Enqueues suspending functions in the Queue.
     */
    fun enqueueSuspendTask(task: suspend () -> Unit) {
        taskQueue.offer(task)
        if (!isProcessing) {
            processQueue()
        }
    }

    /**
     * Processes elements from the Queue one by one based on the order they were inserted.
     */
    private fun processQueue() {
        isProcessing = true
        CoroutineScope(coroutineDispatcherProvider.io()).launch {
            while (taskQueue.isNotEmpty()) {
                val task = taskQueue.poll()
                task?.invoke()
            }
            isProcessing = false
        }
    }

    fun enqueueOperationTask(networkOperationTask: NetworkOperationTask<*>) {
        val task: suspend () -> Unit = {
            val result = networkOperationTask.invoke()
            // Handle result if needed
        }
        taskQueue.offer(task)
    }

    fun <T> executeRequestOnWifi(
        context: Context,
        request: suspend () -> T,
        callback: (Result<T>) -> Unit
    ) {
        if (isConnectedToWifi(context)) {
            // Connected to Wi-Fi, execute request immediately
            executeRequest(request, callback)
        } else {
            // Not connected to Wi-Fi, wait and check again
            mainHandler.postDelayed({
                executeRequestOnWifi(context, request, callback)
            }, 5000) // Wait for 5 second before checking again
        }
    }

    private fun <T> executeRequest(request: suspend () -> T, callback: (Result<T>) -> Unit) {
        CoroutineScope(coroutineDispatcherProvider.io()).launch {
            try {
                val result = request()
                withContext(coroutineDispatcherProvider.io()) {
                    callback(Result.success(result))
                }
            } catch (e: Exception) {
                withContext(coroutineDispatcherProvider.io()) {
                    callback(Result.failure(e))
                }
            }
        }
    }

    fun cancelAllRequests() {
        // Implement cancellation logic if needed
    }
}

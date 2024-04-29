package com.example.hanadroid.networking

import okhttp3.Interceptor
import okhttp3.Response
import okio.IOException

/**
 * Makes use of Retrofit Client to for retry mechanism with exponential back-off strategy.
 *
 * **How Exponential Backoff works?**
 *
 * The `Thread.sleep(nextRetryDelay)` line in the RetryInterceptor implementation is used
 * to introduce a delay before retrying the failed request. This delay is part of the
 * exponential backoff strategy, which is a technique used to prevent overwhelming
 * the server with too many retries in a short period.
 *
 * **Here's what happens when Thread.sleep(nextRetryDelay) is executed:**
 *
 * The current thread (in this case, the thread executing the Retrofit request) is put into a
 * sleeping state for the duration specified by nextRetryDelay.
 * During this sleeping period, the thread is considered blocked and cannot perform any other operations.
 * After the specified delay (nextRetryDelay), the thread resumes execution from where it left off.
 *
 * The purpose of introducing this delay is to give the server some time to recover or to handle
 * the incoming requests more efficiently. If a retry is attempted immediately after a failure,
 * it can potentially put more load on the server, exacerbating the issue and causing further failures.
 *
 * By implementing an exponential backoff strategy, the delay between retries increases exponentially.
 *
 * For example,
 * if the initial delay is set to 1 second, and the retryBackoffMultiplier is set to 1.5, the delays would be:
 *
 * - First retry: 1 second
 * - Second retry: 1.5 seconds
 * - Third retry: 2.25 seconds
 * - Fourth retry: 3.375 seconds
 * ... and so on.
 *
 * This exponential increase in delay time helps to reduce the load on the server and gives
 * it more time to recover or handle the requests more effectively.
 */
class RetryInterceptor(
    private val maxRetries: Int = 3,
    private val retryDelayMillis: Long = 1000,
    private val retryBackoffMultiplier: Float = 1.5f
) : Interceptor {

    private var retryCount = 0

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        lateinit var response: Response
        var nextRetryDelay = retryDelayMillis

        while (retryCount < maxRetries) {
            try {
                response = chain.proceed(request)
            } catch (e: IOException) {
                retryCount++
                nextRetryDelay = (nextRetryDelay * retryBackoffMultiplier).toLong()
                Thread.sleep(nextRetryDelay)
                request = request.newBuilder().build()
                continue
            }

            if (response.isSuccessful) {
                return response
            }

            // Handle non-successful responses here, e.g., 500 Internal Server Error
            if (response.code == 500) {
                retryCount++
                nextRetryDelay = (nextRetryDelay * retryBackoffMultiplier).toLong()
                Thread.sleep(nextRetryDelay)
                request = request.newBuilder().build()
            } else {
                return response
            }
        }

        return response
    }
}
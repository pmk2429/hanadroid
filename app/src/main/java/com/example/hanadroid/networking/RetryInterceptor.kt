package com.example.hanadroid.networking

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Makes use of Retrofit Client to for retry mechanism.
 */
class RetryInterceptor(private val maxRetries: Int) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        var response = chain.proceed(request)
        var retryCount = 0

        while (!response.isSuccessful && retryCount < maxRetries) {
            retryCount++
            request = request.newBuilder().build()
            response.close() // Important: Close previous response body to avoid leaks
            response = chain.proceed(request)
        }

        return response
    }
}

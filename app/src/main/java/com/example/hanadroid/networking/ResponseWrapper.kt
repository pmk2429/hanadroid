package com.example.hanadroid.networking

/**
 * Response wrapper that determines the type of Response received.
 */
sealed class ResponseWrapper<out RESULT> {
    data class Success<out T>(val data: T) : ResponseWrapper<T>()
    data class Error(val failureMessage: String) : ResponseWrapper<Nothing>()
}

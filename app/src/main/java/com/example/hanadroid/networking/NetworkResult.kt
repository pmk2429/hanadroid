package com.example.hanadroid.networking

/**
 * Response wrapper that determines the type of Response received.
 */
sealed class ResponseWrapper<out RESULT> {
    data class Success<out T>(val data: T) : ResponseWrapper<T>()
    data class Error(val failureMessage: String) : ResponseWrapper<Nothing>()
}

sealed class NetworkResult<out RESULT> {
    data class Success<T : Any>(val data: T) : NetworkResult<T>()
    data class Error<T : Any>(val code: Int, val message: String?) : NetworkResult<T>()
    data class Exception<T : Any>(val e: Throwable) : NetworkResult<T>()
}

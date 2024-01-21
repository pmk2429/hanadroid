package com.example.hanadroid.networking

/**
 * Represents Error Response from the API:
 * {
 *    “status”: ”UnSuccessful”,
 *    “failure_message” : ”Invalid api key or api key expires”
 * }
 */
data class ErrorResponse(
    val status: String? = null,
    val failureMessage: String? = null
)

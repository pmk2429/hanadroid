package com.example.hanadroid.util

import kotlinx.coroutines.CoroutineDispatcher

interface CoroutineDispatcherProvider {
    fun io(): CoroutineDispatcher
    fun main(): CoroutineDispatcher
    fun default(): CoroutineDispatcher
    fun unconfined(): CoroutineDispatcher
}

package com.example.hanadroid.cache

internal interface CacheStore<Key, Output> {
    fun get(key: Key): Output?

    fun store(key: Key, output: Output): Output?

    fun hasKey(key: Key): Boolean

    fun clear(key: Key)

    fun clear()
}

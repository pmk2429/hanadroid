package com.example.hanadroid.cache

import java.util.concurrent.TimeUnit

open class InMemoryLruCacheStore<Key, Value>(
    private val cacheSize: Int = 50
) : CacheStore<Key, Value> {

    private val backingStorage =
        object : LinkedHashMap<Key, Value>(cacheSize, MAP_LOAD_FACTOR, true) {
            override fun removeEldestEntry(
                eldest: MutableMap.MutableEntry<Key, Value>?
            ): Boolean {
                return this.size > cacheSize
            }
        }

    override fun store(key: Key, output: Value): Value? =
        backingStorage.put(key = key, value = output)

    override fun get(key: Key): Value? {
        return if (backingStorage.containsKey(key)) backingStorage[key] else null
    }

    override fun hasKey(key: Key): Boolean {
        return backingStorage.containsKey(key)
    }

    override fun clear(key: Key) {
        if (backingStorage.containsKey(key)) {
            backingStorage.remove(key)
        }
    }

    override fun clear() {
        backingStorage.clear()
    }

    companion object {
        internal const val MAP_LOAD_FACTOR = 0.75f
    }
}

/**
 * In-memory cache with TTL on the contained values.
 */
internal class TTLInMemoryLruCacheStore<Key, Value>(
    private val ttlInMs: Long = TimeUnit.MINUTES.toMillis(10),
    cacheSize: Int = 50
) : InMemoryLruCacheStore<Key, Value>(cacheSize) {

    private val ttlStorage = object : LinkedHashMap<Key, Long>(cacheSize, MAP_LOAD_FACTOR, true) {
        override fun removeEldestEntry(
            eldest: MutableMap.MutableEntry<Key, Long>?
        ): Boolean {
            return this.size > cacheSize
        }
    }

    override fun store(key: Key, output: Value): Value? {
        val result = super.store(key, output)
        return result?.apply {
            ttlStorage[key] = System.currentTimeMillis()
        }
    }

    override fun get(key: Key): Value? {
        if (needsEviction(key)) {
            tryEvictData(key)
            return null
        }
        return super.get(key)
    }

    override fun hasKey(key: Key): Boolean {
        if (needsEviction(key)) {
            tryEvictData(key)
            return false
        }
        return super.hasKey(key)
    }

    private fun needsEviction(key: Key): Boolean {
        // can't evict if item not available
        if (ttlStorage.containsKey(key)) {
            val timestamp = ttlStorage[key] ?: return false
            return System.currentTimeMillis().minus(ttlInMs) > timestamp
        } else {
            // key not found so no eviction needed
            return false
        }
    }

    private fun tryEvictData(key: Key): Boolean {
        if (ttlStorage.containsKey(key)) {
            return false
        }
        // evict the item from Cache
        ttlStorage.remove(key)
        clear(key)
        return true
    }
}

internal suspend fun <Key, Value> InMemoryLruCacheStore<Key, Value>.getOrFetchAndSave(
    key: Key,
    fetcher: suspend () -> Value
): Value? {
    val cachedItem = this.get(key)
    if (cachedItem != null) {
        return cachedItem
    }
    // item is not cached, so fetch it and create it
    val fetchedItem = fetcher()
    return store(key, fetchedItem)
}
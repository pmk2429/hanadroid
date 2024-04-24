package com.example.hanadroid.caches

import android.graphics.Bitmap
import android.util.LruCache

class InMemoryCache(private val maxSize: Int) {

    private val cache: LruCache<String, Bitmap> = object : LruCache<String, Bitmap>(maxSize) {
        override fun sizeOf(key: String, value: Bitmap): Int {
            return value.byteCount / 1024
        }
    }

    fun put(key: String, bitmap: Bitmap) {
        cache.put(key, bitmap)
    }

    fun get(key: String): Bitmap? {
        return cache.get(key)
    }

    fun clear() {
        cache.evictAll()
    }
}

// Usage
val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
val cacheSize = maxMemory / 8
val memoryCache = InMemoryCache(cacheSize)

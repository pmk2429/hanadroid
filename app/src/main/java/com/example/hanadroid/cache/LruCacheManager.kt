package com.example.hanadroid.cache

import android.graphics.Bitmap
import android.util.LruCache

class LruCacheManager {

    companion object {
        private const val CACHE_SIZE_PERCENTAGE = 20 // Percentage of available heap size
        private val maxMemory = Runtime.getRuntime().maxMemory().toInt()
        private val cacheSize = maxMemory / 100 * CACHE_SIZE_PERCENTAGE

        private val bitmapCache = LruCache<String, Bitmap>(cacheSize)

        fun addBitmapToCache(key: String, bitmap: Bitmap) {
            bitmapCache.put(key, bitmap)
        }

        fun getBitmapFromCache(key: String): Bitmap? {
            return bitmapCache.get(key)
        }
    }
}

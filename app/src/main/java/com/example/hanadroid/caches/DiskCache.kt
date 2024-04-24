package com.example.hanadroid.caches

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream

class DiskCache(
    private val context: Context,
    private val cacheDirName: String,
    private val cacheSize: Long
) {

    private val diskCacheDir: File by lazy {
        File(context.cacheDir, cacheDirName)
    }

    init {
        if (!diskCacheDir.exists()) {
            diskCacheDir.mkdirs()
        }
    }

    fun put(key: String, bitmap: Bitmap) {
        val file = File(diskCacheDir, key)
        FileOutputStream(file).use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
            it.flush()
        }
    }

    fun get(key: String): Bitmap? {
        val file = File(diskCacheDir, key)
        if (file.exists()) {
            return BitmapFactory.decodeFile(file.absolutePath)
        }
        return null
    }

    fun clear() {
        val files = diskCacheDir.listFiles() ?: return
        for (file in files) {
            file.delete()
        }
    }

    fun init() {
        val cacheDirName = "my_cache"
        val cacheSize = 10 * 1024 * 1024 // 10 MiB
        val diskCache = DiskCache(context, cacheDirName, cacheSize.toLong())
    }
}
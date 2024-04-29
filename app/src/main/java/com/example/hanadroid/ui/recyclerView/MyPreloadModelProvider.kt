package com.example.hanadroid.ui.recyclerView

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.ListPreloader
import com.bumptech.glide.RequestBuilder

class MyPreloadModelProvider(
    private val context: Context,
    private val imageUrls: List<String> = emptyList()
) : ListPreloader.PreloadModelProvider<String> {

    private val imageWidthPixels = 1024
    private val imageHeightPixels = 768
    override fun getPreloadItems(position: Int): MutableList<String> {
        val url = imageUrls.getOrNull(position) ?: return mutableListOf()
        return mutableListOf(url)
    }

    override fun getPreloadRequestBuilder(item: String): RequestBuilder<*>? {
        return Glide.with(context)
            .load(item)
            .override(imageWidthPixels, imageHeightPixels)
    }
}
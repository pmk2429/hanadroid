package com.example.hanadroid.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.example.hanadroid.databinding.AdapterRedditPostItemBinding
import com.example.hanadroid.model.RedditPost
import com.example.hanadroid.util.DiffUtilCallBack

class RedditPostAdapter : PagingDataAdapter<RedditPost, RedditRepoViewHolder>(DiffUtilCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RedditRepoViewHolder {
        val binding =
            AdapterRedditPostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RedditRepoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RedditRepoViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(redditPost = it)
        }
    }
}

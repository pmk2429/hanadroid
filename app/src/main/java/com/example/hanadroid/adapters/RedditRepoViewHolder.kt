package com.example.hanadroid.adapters

import androidx.recyclerview.widget.RecyclerView
import com.example.hanadroid.databinding.AdapterRedditPostItemBinding
import com.example.hanadroid.model.RedditPost


class RedditRepoViewHolder(
    private val binding: AdapterRedditPostItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(redditPost: RedditPost) {
        binding.apply {
            title.text = redditPost.title
            comments.text = redditPost.commentCount.toString()
            score.text = redditPost.score.toString()

            // title.text = "http://example.com"
            // Linkify.addLinks(title, Linkify.WEB_URLS)
            // Linkify.addLinks(title, Linkify.ALL);
        }
    }
}

package com.example.hanadroid.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hanadroid.databinding.StoryNewsFeedItemLayoutBinding
import com.hana.nextdoor.models.Story
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class StoryNewsFeedAdapter @Inject constructor(
    // item click listener
) : ListAdapter<Story, StoryNewsFeedAdapter.StoryItemViewHolder>(STORY_ITEM_DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryItemViewHolder {
        val binding = StoryNewsFeedItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return StoryItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryItemViewHolder, position: Int) {
        val storyItem = getItem(position)
        holder.bind(storyItem)
    }

    class StoryItemViewHolder(
        private val binding: StoryNewsFeedItemLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: Story) {
            with(binding) {
                author.text = story.author
                description.text = story.body
                Glide.with(storyImage)
                    .load(story.imageSpec.url)
                    .fitCenter()
                    .into(storyImage)
            }
        }
    }

    companion object {
        private val STORY_ITEM_DIFF_UTIL = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }
        }
    }
}

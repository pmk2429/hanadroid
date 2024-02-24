package com.example.hanadroid.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.hanadroid.databinding.MarsDataItemLayoutBinding
import com.example.hanadroid.model.MarsPhoto
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class MarsDataListAdapter @Inject constructor(
    // itemClickListener
) : ListAdapter<MarsPhoto, MarsDataListAdapter.MarsDataListItemViewHolder>(MARS_DATA_DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarsDataListItemViewHolder {
        val binding = MarsDataItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MarsDataListItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MarsDataListItemViewHolder, position: Int) {
        val marsPhotoItem = getItem(position)
        holder.bind(marsPhotoItem)
    }

    class MarsDataListItemViewHolder(
        private val binding: MarsDataItemLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(marsPhoto: MarsPhoto) {
            with(binding) {
                Glide.with(marsRealEstateImage)
                    .load(marsPhoto.imgSrcUrl)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(marsRealEstateImage)
                identifier.text = marsPhoto.id
            }
        }

    }

    companion object {
        private val MARS_DATA_DIFF_UTIL = object : DiffUtil.ItemCallback<MarsPhoto>() {
            override fun areItemsTheSame(oldItem: MarsPhoto, newItem: MarsPhoto): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MarsPhoto, newItem: MarsPhoto): Boolean {
                return oldItem == newItem
            }

        }
    }
}
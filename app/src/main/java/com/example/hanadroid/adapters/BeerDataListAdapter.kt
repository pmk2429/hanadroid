package com.example.hanadroid.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hanadroid.databinding.BeerDataListItemLayoutBinding
import com.example.hanadroid.model.BeerInfo
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class BeerDataListAdapter @Inject constructor(
    val itemClickListener: BeerItemClickListener
) : ListAdapter<BeerInfo, BeerDataListAdapter.BeerItemViewHolder>(BeerDataDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeerItemViewHolder {
        val binding = BeerDataListItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BeerItemViewHolder(binding, itemClickListener)
    }

    override fun onBindViewHolder(holder: BeerItemViewHolder, position: Int) {
        val beerItem = getItem(position)
        holder.bind(beerItem)
    }

    class BeerDataDiffUtil : DiffUtil.ItemCallback<BeerInfo>() {
        override fun areItemsTheSame(oldItem: BeerInfo, newItem: BeerInfo): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: BeerInfo, newItem: BeerInfo): Boolean {
            return oldItem == newItem
        }
    }

    class BeerItemClickListener @Inject constructor() {
        var onItemClick: ((BeerInfo) -> Unit)? = null
        fun onBeerItemClicked(data: BeerInfo) {
            onItemClick?.invoke(data)
        }
    }

    class BeerItemViewHolder(
        private val binding: BeerDataListItemLayoutBinding,
        private val clickListener: BeerItemClickListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(beerInfo: BeerInfo) {
            with(binding) {
                itemClickListener = clickListener
                beerItem = beerInfo
                beerName.text = beerInfo.name
                beerTagline.text = beerInfo.tagline
                beerTips.text = beerInfo.brewerTips
            }
        }
    }
}
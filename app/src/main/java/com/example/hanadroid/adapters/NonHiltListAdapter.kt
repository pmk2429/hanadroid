package com.example.hanadroid.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hanadroid.databinding.SimpleItemListItemLayoutBinding
import com.example.hanadroid.model.SampleItem

class NonHiltListAdapter :
    ListAdapter<SampleItem, NonHiltListAdapter.ItemViewHolder>(ItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = SimpleItemListItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private class ItemDiffCallback : DiffUtil.ItemCallback<SampleItem>() {
        override fun areItemsTheSame(oldItem: SampleItem, newItem: SampleItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SampleItem, newItem: SampleItem): Boolean {
            return oldItem == newItem
        }
    }

    class ItemViewHolder(
        val binding: SimpleItemListItemLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SampleItem) {
            with(binding) {
                simpleItemName.text = item.name
            }
        }
    }
}

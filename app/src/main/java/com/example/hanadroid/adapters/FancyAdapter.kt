package com.example.hanadroid.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hanadroid.databinding.FancyItemLayoutBinding
import com.example.hanadroid.model.FancyModel
import com.example.hanadroid.model.toFormattedDateString
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class FancyAdapter @Inject constructor(
    private val itemClick: () -> Unit
) : ListAdapter<FancyModel, RecyclerView.ViewHolder>(FANCY_ITEM_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            FancyItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FancyViewHolder(binding, itemClick)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is FancyViewHolder -> {
                holder.bind(item)
            }

            else -> {
                throw IllegalArgumentException("Wrong argument for ViewHolder")
            }
        }
    }

    class FancyViewHolder(
        val binding: FancyItemLayoutBinding,
        private val itemClick: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(fancyItem: FancyModel) {
            with(binding) {
                fancyItemNumber.text = fancyItem.randomNum.toString()
                fancyItemName.text = fancyItem.content
                fancyItemCreatedAt.text = fancyItem.createdAt.toFormattedDateString()
                root.setOnClickListener { itemClick.invoke() }
            }
        }
    }

    companion object {
        private val FANCY_ITEM_COMPARATOR = object : DiffUtil.ItemCallback<FancyModel>() {
            override fun areItemsTheSame(oldItem: FancyModel, newItem: FancyModel): Boolean =
                oldItem.content == newItem.content

            override fun areContentsTheSame(oldItem: FancyModel, newItem: FancyModel): Boolean =
                oldItem == newItem
        }
    }
}

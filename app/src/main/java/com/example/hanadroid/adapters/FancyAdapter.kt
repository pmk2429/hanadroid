package com.example.hanadroid.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hanadroid.databinding.FancyItemLayoutBinding
import com.example.hanadroid.model.FancyModel
import com.example.hanadroid.model.createdText
import com.example.hanadroid.model.toFormattedDateString
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class FancyAdapter @Inject constructor(

) : ListAdapter<FancyModel, RecyclerView.ViewHolder>(DiffUtilCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            FancyItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FancyViewHolder(binding)
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

    class FancyViewHolder(val binding: FancyItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(fancyItem: FancyModel) {
            with(binding) {
                fancyItemNumber.text = fancyItem.randomNum.toString()
                fancyItemName.text = fancyItem.content
                fancyItemCreatedAt.text = fancyItem.createdAt.toFormattedDateString()
            }
        }
    }

    class DiffUtilCallBack : DiffUtil.ItemCallback<FancyModel>() {
        override fun areItemsTheSame(oldItem: FancyModel, newItem: FancyModel): Boolean {
            return oldItem.content == newItem.content
        }

        override fun areContentsTheSame(oldItem: FancyModel, newItem: FancyModel): Boolean {
            return oldItem == newItem
        }

    }
}
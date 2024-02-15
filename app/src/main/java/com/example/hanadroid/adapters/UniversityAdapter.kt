package com.example.hanadroid.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hanadroid.databinding.UniversityItemLayoutBinding
import com.example.hanadroid.model.University
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class UniversityAdapter @Inject constructor(
    var universityItemClickListener: UniversityItemClickListener
) : ListAdapter<University, UniversityAdapter.UniversityViewHolder>(UNIVERSITY_ITEM_DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UniversityViewHolder {
        val binding =
            UniversityItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UniversityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UniversityViewHolder, position: Int) {
        holder.bind(
            university = getItem(position),
            universityItemClickListener = universityItemClickListener
        )
    }

    class UniversityItemClickListener @Inject constructor() {
        var onItemClick: ((University) -> Unit)? = null

        fun onUniversityClick(university: University) {
            onItemClick?.invoke(university)
        }
    }

    class UniversityViewHolder(
        private val binding: UniversityItemLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(university: University, universityItemClickListener: UniversityItemClickListener) {
            with(binding) {
                universityItem = university
                itemView.setOnClickListener {
                    universityItemClickListener.onUniversityClick(university)
                }
                itemClickListener = universityItemClickListener
                executePendingBindings()
            }
        }
    }

    companion object {
        private val UNIVERSITY_ITEM_DIFF_UTIL = object : DiffUtil.ItemCallback<University>() {
            override fun areItemsTheSame(oldItem: University, newItem: University): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: University, newItem: University): Boolean {
                return oldItem == newItem
            }

        }
    }
}


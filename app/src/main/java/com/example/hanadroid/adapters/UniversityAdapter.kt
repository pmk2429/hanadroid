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
) : ListAdapter<University, UniversityAdapter.UniversityViewHolder>(UniversityListDiffUtil()) {

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

    class UniversityListDiffUtil : DiffUtil.ItemCallback<University>() {
        override fun areItemsTheSame(oldItem: University, newItem: University): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: University, newItem: University): Boolean {
            return oldItem == newItem
        }

    }

    class UniversityItemClickListener @Inject constructor() {
        var onItemClick: ((University) -> Unit)? = null

        fun onUniversityClick(data: University) {
            onItemClick?.invoke(data)
        }
    }

    class UniversityViewHolder(
        private val binding: UniversityItemLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(university: University, universityItemClickListener: UniversityItemClickListener) {
            with(binding) {
                universityItem = university
                itemClickListener = universityItemClickListener
                executePendingBindings()
            }
        }
    }
}


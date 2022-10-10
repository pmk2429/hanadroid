package com.example.hanadroid.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hanadroid.model.University
import com.example.hanadroid.databinding.UniversityItemLayoutBinding

class UniversityAdapter(
    private var universities: List<University>,
    private val universityItemClickListener: UniversityItemClickListener
) : RecyclerView.Adapter<UniversityAdapter.UniversityViewHolder>() {

    interface UniversityItemClickListener {
        fun onUniversityClicked(university: University)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UniversityViewHolder {
        val binding =
            UniversityItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UniversityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UniversityViewHolder, position: Int) {
        holder.bind(university = universities[position])
    }

    override fun getItemCount(): Int = universities.size

    fun setUniversities(updatedUniversities: List<University>) {
        universities = updatedUniversities
        notifyDataSetChanged()
    }

    inner class UniversityViewHolder(
        private val binding: UniversityItemLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(university: University) {
            binding.apply {
                universityName.text = university.name
                webUrl.text = university.domains[0]
                country.text = university.country
                universityContainer.setOnClickListener {
                    universityItemClickListener.onUniversityClicked(university)
                }
            }
        }
    }
}
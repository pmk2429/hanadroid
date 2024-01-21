package com.example.hanadroid.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hanadroid.databinding.ImageCarouselLayoutBinding
import com.example.hanadroid.model.HanaImage

class ImageCarouselAdapter(
    private val items: List<HanaImage>
) : RecyclerView.Adapter<ImageCarouselAdapter.ImageCarouselItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageCarouselItemViewHolder {
        val binding = ImageCarouselLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ImageCarouselItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageCarouselItemViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ImageCarouselItemViewHolder(
        val binding: ImageCarouselLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(imageItem: HanaImage) {
            with(binding) {
                val imageAdapter = ImagePagerAdapter(imageItem.imageUrls)
                carouselViewPager.adapter = imageAdapter
            }
        }
    }
}

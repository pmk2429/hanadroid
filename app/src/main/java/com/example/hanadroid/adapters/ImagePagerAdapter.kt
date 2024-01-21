package com.example.hanadroid.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hanadroid.databinding.ImageCarouselItemLayoutBinding

/**
 * Adapter to provide for ViewPager2
 */
class ImagePagerAdapter(
    private val imageUrls: List<String>
) : RecyclerView.Adapter<ImagePagerAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ImageCarouselItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUrl = imageUrls[position]
        holder.bind(imageUrl)
    }

    override fun getItemCount(): Int {
        return imageUrls.size
    }

    inner class ImageViewHolder(
        val binding: ImageCarouselItemLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(imageUrl: String) {
            with(binding) {
                Glide.with(carouselImageView)
                    .load(imageUrl)
                    .centerInside()
                    .into(carouselImageView)
            }
        }
    }
}

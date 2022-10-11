package com.example.hanadroid.adapters

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hanadroid.databinding.RickMortyCharacterRecyclerRowItemBinding
import com.example.hanadroid.model.RickMortyCharacter

class CharactersViewHolder(
    private val binding: RickMortyCharacterRecyclerRowItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(rickMortyCharacter: RickMortyCharacter) {
        binding.apply {
            name.text = rickMortyCharacter.name
            species.text = rickMortyCharacter.species
            Glide.with(avatar)
                .load(rickMortyCharacter.imageUrl)
                .circleCrop()
                .into(avatar)
        }
    }
}

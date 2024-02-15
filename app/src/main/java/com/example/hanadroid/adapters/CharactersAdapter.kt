package com.example.hanadroid.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.hanadroid.databinding.RickMortyCharacterRecyclerRowItemBinding
import com.example.hanadroid.model.RickMortyCharacter

class CharactersAdapter(
) : PagingDataAdapter<RickMortyCharacter, CharactersViewHolder>(CHARACTERS_DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersViewHolder {
        val binding = RickMortyCharacterRecyclerRowItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CharactersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) {
        val rickMortyCharacter = getItem(position)
        rickMortyCharacter?.let {
            holder.bind(it)
        }
    }

    companion object {
        private val CHARACTERS_DIFF_UTIL = object : DiffUtil.ItemCallback<RickMortyCharacter>() {
            override fun areItemsTheSame(
                oldItem: RickMortyCharacter,
                newItem: RickMortyCharacter
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: RickMortyCharacter,
                newItem: RickMortyCharacter
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}

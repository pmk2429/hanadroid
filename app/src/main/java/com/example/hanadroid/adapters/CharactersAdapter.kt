package com.example.hanadroid.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.example.hanadroid.databinding.RickMortyCharacterRecyclerRowItemBinding
import com.example.hanadroid.model.RickMortyCharacter
import com.example.hanadroid.util.DiffUtilCallBack

class CharactersAdapter :
    PagingDataAdapter<RickMortyCharacter, CharactersViewHolder>(DiffUtilCallBack()) {

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
}

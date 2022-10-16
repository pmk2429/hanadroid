package com.example.hanadroid.util

import androidx.recyclerview.widget.DiffUtil
import com.example.hanadroid.model.RickMortyCharacter

class DiffUtilCallBack : DiffUtil.ItemCallback<RickMortyCharacter>() {
    override fun areItemsTheSame(
        oldItem: RickMortyCharacter,
        newItem: RickMortyCharacter
    ): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(
        oldItem: RickMortyCharacter,
        newItem: RickMortyCharacter
    ): Boolean {
        return oldItem == newItem
    }
}

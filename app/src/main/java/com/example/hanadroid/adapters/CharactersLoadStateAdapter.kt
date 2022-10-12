package com.example.hanadroid.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.example.hanadroid.databinding.CharactersLoadStateFooterViewItemBinding

class CharactersLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<CharactersLoadStateViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): CharactersLoadStateViewHolder {
        val binding = CharactersLoadStateFooterViewItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CharactersLoadStateViewHolder(binding, retry)
    }

    override fun onBindViewHolder(holder: CharactersLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }
}
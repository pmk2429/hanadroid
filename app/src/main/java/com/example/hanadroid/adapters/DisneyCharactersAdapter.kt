package com.example.hanadroid.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hanadroid.databinding.DisneyImageListItemLayoutBinding
import com.example.hanadroid.databinding.DisneyTextListItemLayoutBinding
import com.example.hanadroid.model.DisneyCharacter
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class DisneyCharactersAdapter @Inject constructor(

) : ListAdapter<DisneyCharacter, RecyclerView.ViewHolder>(
    DisneyCharacterListDiffUtil()
) {

    companion object {
        const val VIEW_TYPE_TEXT = 1
        const val VIEW_TYPE_IMAGE = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_TEXT -> {
                val binding = DisneyTextListItemLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                CharacterTextViewHolder(binding)
            }
            VIEW_TYPE_IMAGE -> {
                val binding = DisneyImageListItemLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                CharacterImageViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CharacterImageViewHolder -> {
                holder.bind(character = getItem(position))
            }
            is CharacterTextViewHolder -> {
                holder.bind(character = getItem(position))
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if (item.tvShows.isNotEmpty()) {
            VIEW_TYPE_TEXT
        } else {
            VIEW_TYPE_IMAGE
        }
    }

    class DisneyCharacterListDiffUtil : DiffUtil.ItemCallback<DisneyCharacter>() {

        override fun areItemsTheSame(oldItem: DisneyCharacter, newItem: DisneyCharacter): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(
            oldItem: DisneyCharacter,
            newItem: DisneyCharacter
        ): Boolean {
            return oldItem == newItem
        }
    }

    class CharacterTextViewHolder(
        private val binding: DisneyTextListItemLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            character: DisneyCharacter
        ) {
            with(binding) {
                characterName.text = character.name
                tvShowName.text = character.tvShows[0]
            }
        }
    }

    class CharacterImageViewHolder(
        private val binding: DisneyImageListItemLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            character: DisneyCharacter
        ) {
            with(binding) {
                characterName.text = character.name
                Glide.with(characterImage)
                    .load(character.imageUrl)
                    .circleCrop()
                    .into(characterImage)
            }
        }
    }
}

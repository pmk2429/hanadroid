package com.example.hanadroid.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.hanadroid.databinding.DisneyCharacterListItemLayoutBinding
import com.example.hanadroid.model.DisneyCharacter
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class DisneyCharactersAdapter @Inject constructor(
    val characterItemClickListener: CharacterItemClickListener
) : ListAdapter<DisneyCharacter, DisneyCharactersAdapter.DisneyCharacterViewHolder>(
    DISNEY_CHARACTER_ITEM_DIFF
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DisneyCharacterViewHolder {
        val binding = DisneyCharacterListItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DisneyCharacterViewHolder(binding, characterItemClickListener)
    }

    override fun onBindViewHolder(holder: DisneyCharacterViewHolder, position: Int) {
        val character = getItem(position)
        holder.bind(character)
    }

    class DisneyCharacterViewHolder(
        private val binding: DisneyCharacterListItemLayoutBinding,
        private val itemClickListener: CharacterItemClickListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(character: DisneyCharacter) {
            with(binding) {
                // name
                characterName.text = character.name

                // image
                Glide.with(characterImage)
                    .load(character.imageUrl)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(characterImage)

                // movies
                character.films.apply {
                    characterMovies.isVisible = isNotEmpty()
                    moviesLabel.isVisible = isNotEmpty()
                    val movies = joinToString(",")
                    characterMovies.text = movies
                }

                // TV Shows
                character.tvShows.apply {
                    characterTvShows.isVisible = isNotEmpty()
                    tvShowsLabel.isVisible = isNotEmpty()
                    val shows = joinToString(",")
                    characterTvShows.text = shows
                }

                // Games
                character.videoGames.apply {
                    characterGames.isVisible = isNotEmpty()
                    gamesLabel.isVisible = isNotEmpty()
                    val games = joinToString(",")
                    characterGames.text = games
                }

                // toggle visibility of Favorite icon
                favoriteCharacter.isVisible = character.isFavorite

                // handle onClick listener
                itemView.setOnClickListener {
                    character.isFavorite = !character.isFavorite
                    favoriteCharacter.isVisible = character.isFavorite
                    itemClickListener.onCharacterClick(character)
                }
            }
        }
    }

    class CharacterItemClickListener @Inject constructor() {
        var onItemClick: ((DisneyCharacter) -> Unit)? = null

        fun onCharacterClick(character: DisneyCharacter) {
            onItemClick?.invoke(character)
        }
    }

    companion object {
        private val DISNEY_CHARACTER_ITEM_DIFF = object : DiffUtil.ItemCallback<DisneyCharacter>() {
            override fun areItemsTheSame(
                oldItem: DisneyCharacter,
                newItem: DisneyCharacter
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: DisneyCharacter,
                newItem: DisneyCharacter
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}

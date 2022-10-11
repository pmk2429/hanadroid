package com.example.hanadroid.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.hanadroid.model.RickMortyCharacter

@Dao
interface RickMortyCharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repos: List<RickMortyCharacter>)

    @Query(
        "SELECT * FROM rick_morty_characters WHERE " +
                "name LIKE :queryString " +
                "ORDER BY name ASC"
    )
    fun charactersByName(queryString: String): PagingSource<Int, RickMortyCharacter>

    @Query("SELECT * from rick_morty_characters")
    fun getCharacters(): PagingSource<Int, RickMortyCharacter>

    @Query("DELETE FROM rick_morty_characters")
    suspend fun clearAllCharacters()
}

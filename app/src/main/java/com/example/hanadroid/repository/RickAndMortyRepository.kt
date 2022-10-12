package com.example.hanadroid.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.hanadroid.data.rickandmorty.RickAndMortyApiService
import com.example.hanadroid.db.RickMortyDatabase
import com.example.hanadroid.mediator.RickMortyRemoteMediator
import com.example.hanadroid.model.RickMortyCharacter
import kotlinx.coroutines.flow.Flow

class RickAndMortyRepository(
    private val service: RickAndMortyApiService,
    private val database: RickMortyDatabase
) {

    fun getAllCharacters(): Flow<PagingData<RickMortyCharacter>> {
        val pagingSourceFactory = { database.charactersDao().getCharacters() }

        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            remoteMediator = RickMortyRemoteMediator(
                service,
                database
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }
}

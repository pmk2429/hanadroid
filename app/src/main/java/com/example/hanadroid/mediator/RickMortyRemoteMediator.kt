package com.example.hanadroid.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.hanadroid.data.rickandmorty.RickAndMortyApiService
import com.example.hanadroid.db.RemoteKeys
import com.example.hanadroid.db.RickMortyDatabase
import com.example.hanadroid.model.RickMortyCharacter
import retrofit2.HttpException
import java.io.IOException

private const val RICK_MORTY_STARTING_PAGE_INDEX = 1

@OptIn(ExperimentalPagingApi::class)
class RickMortyRemoteMediator(
    private val apiService: RickAndMortyApiService,
    private val database: RickMortyDatabase
) : RemoteMediator<Int, RickMortyCharacter>() {

    override suspend fun initialize(): InitializeAction {
        // Launch remote refresh as soon as paging starts and do not trigger remote prepend or
        // append until refresh has succeeded. In cases where we don't mind showing out-of-date,
        // cached offline data, we can return SKIP_INITIAL_REFRESH instead to prevent paging
        // triggering remote refresh.
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, RickMortyCharacter>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> 1
            LoadType.PREPEND ->
                return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKeys != null
                )
                nextKey
            }
        }

        try {
            val apiResponse = apiService.getCharacters(page)
            val resBody = apiResponse.body()
            val pageInfo = resBody?.info
            val allCharacters = resBody?.results

            val endOfPaginationReached = allCharacters!!.isEmpty()
            database.withTransaction {
                // clear all tables in the database in case of user input - refresh
                if (loadType == LoadType.REFRESH) {
                    database.remoteKeysDao().clearRemoteKeys()
                    database.charactersDao().clearAllCharacters()
                }
                val prevKey = if (page == RICK_MORTY_STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val remoteKeys = allCharacters.map {
                    RemoteKeys(charId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                //allCharacters.forEach {
                //    database.remoteKeysDao().insertOrReplace(RemoteKeys(it.id, prevKey, nextKey))
                //}
                database.remoteKeysDao().insertAll(remoteKeys)
                database.charactersDao().insertAll(allCharacters)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, RickMortyCharacter>): RemoteKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { repo ->
            // Get the remote keys of the last item retrieved
            database.remoteKeysDao().remoteKeysRepoId(repo.id)
        }
    }
}

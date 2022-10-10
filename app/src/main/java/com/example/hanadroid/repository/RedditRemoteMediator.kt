package com.example.hanadroid.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.hanadroid.model.RedditPost
import com.example.hanadroid.data.redditapi.RedditApiService
import com.example.hanadroid.db.RedditDatabase
import com.raywenderlich.android.redditclone.models.RedditKeys

@OptIn(ExperimentalPagingApi::class)
class RedditRemoteMediator(
    private val redditService: RedditApiService,
    private val redditDatabase: RedditDatabase
) : RemoteMediator<Int, RedditPost>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, RedditPost>
    ): MediatorResult {
        TODO("Not yet implemented")
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, RedditPost>): RedditKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { post ->
                // Get the remote keys of the last item retrieved
                redditDatabase.redditKeysDao().getRedditKeysByPostId(post.key)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, RedditPost>): RedditKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { post ->
                // Get the remote keys of the first items retrieved
                redditDatabase.redditKeysDao().getRedditKeysByPostId(post.key)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, RedditPost>
    ): RedditKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.key?.let { postId ->
                redditDatabase.redditKeysDao().getRedditKeysByPostId(postId)
            }
        }
    }

}

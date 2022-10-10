package com.example.hanadroid.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.hanadroid.model.RedditPost
import com.example.hanadroid.data.redditapi.RedditApiService
import com.example.hanadroid.db.RedditDatabase
import kotlinx.coroutines.flow.Flow

class RedditRepository(
    private val redditService: RedditApiService,
    private val redditDatabase: RedditDatabase
) {

    @OptIn(ExperimentalPagingApi::class)
    fun getRedditPosts(): Flow<PagingData<RedditPost>> {
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = RedditRemoteMediator(redditService, redditDatabase),
            pagingSourceFactory = { redditDatabase.redditPostsDao().getPosts() }
        ).flow
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 30
    }

}

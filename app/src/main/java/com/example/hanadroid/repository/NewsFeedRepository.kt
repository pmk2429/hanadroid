package com.example.hanadroid.repository

import com.example.hanadroid.networking.NetworkResult
import com.example.hanadroid.util.CoroutineDispatcherProvider
import com.example.hanadroid.data.nextdoor.NewsFeedApiHelper
import com.hana.nextdoor.models.NewsFeedDataResponse
import javax.inject.Inject

class NewsFeedRepository @Inject constructor(
    private val apiHelper: NewsFeedApiHelper,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider
) : BaseRepository(coroutineDispatcherProvider) {

    suspend fun fetchNewsFeed(
        pageId: String?
    ): NetworkResult<NewsFeedDataResponse> {
        return safeApiCall { apiHelper.getNewsFeed(pageId) }
    }

}

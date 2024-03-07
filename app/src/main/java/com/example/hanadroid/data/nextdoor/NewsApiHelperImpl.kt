package com.example.hanadroid.data.nextdoor

import com.hana.nextdoor.models.NewsFeedDataResponse
import retrofit2.Response
import javax.inject.Inject

class NewsApiHelperImpl @Inject constructor(
    private val newsFeedApiService: NewsFeedApiService
) : NewsFeedApiHelper {

    override suspend fun getNewsFeed(pageId: String?): Response<NewsFeedDataResponse> {
        return newsFeedApiService.getNewsFeed(pageId)
    }
}
package com.example.hanadroid.data.nextdoor

import com.hana.nextdoor.models.NewsFeedDataResponse
import retrofit2.Response

interface NewsFeedApiHelper {
    suspend fun getNewsFeed(pageId: String?): Response<NewsFeedDataResponse>
}

package com.example.hanadroid.data.nextdoor

import com.example.hanadroid.networking.FEED_INTERVIEW
import com.hana.nextdoor.models.NewsFeedDataResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface NewsFeedApiService {

    @GET(FEED_INTERVIEW + "page{pageId}.json")
    suspend fun getNewsFeed(@Path("pageId") pageId: String?): Response<NewsFeedDataResponse>
}

package com.example.redditapp.network

import com.example.redditapp.ui.model.SubredditsResponse
import retrofit2.http.GET
import retrofit2.http.Header

interface RedditApiService {
    @GET("subreddits/mine/subscriber?limit=100")
    suspend fun getSubscribedSubreddits(
    ): SubredditsResponse
}

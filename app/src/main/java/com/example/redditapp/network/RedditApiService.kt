package com.example.redditapp.network

import com.example.redditapp.ui.model.SubredditPageResponse
import com.example.redditapp.ui.model.SubredditsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RedditApiService {
    @GET("subreddits/mine/subscriber?limit=100")
    suspend fun getSubscribedSubreddits(
    ): SubredditsResponse

    @GET(".json")
    suspend fun getHomePage(
        @Query("after") after: String
    ): SubredditPageResponse

    @GET("r/{subreddit}/.json")
    suspend fun getSubredditPage(
        @Path("subreddit") subreddit: String,
        @Query("after") after: String
    ): SubredditPageResponse
}

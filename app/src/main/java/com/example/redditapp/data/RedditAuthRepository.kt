package com.example.redditapp.data

import com.example.redditapp.network.RedditApiService
import com.example.redditapp.ui.model.SubredditListingModel
import com.example.redditapp.ui.model.SubredditPageResponse
import com.example.redditapp.ui.model.SubredditsDataModel
import com.example.redditapp.ui.model.SubredditsResponse
import javax.inject.Inject

interface RedditAuthRepository {
    suspend fun getSubscribedSubreddits(): SubredditsDataModel
    suspend fun getHomePage(after: String): List<SubredditListingModel>
    suspend fun getSubredditPage(subreddit: String, after: String): List<SubredditListingModel>
}

class RedditAuthRepositoryImp @Inject constructor() :

    RedditAuthRepository {
    @Inject
    lateinit var redditApiService: RedditApiService

    override suspend fun getSubscribedSubreddits(): SubredditsDataModel {
        var response: SubredditsResponse = redditApiService.getSubscribedSubreddits()
        return response.data
    }

    override suspend fun getHomePage(after: String): List<SubredditListingModel> {
        var response: SubredditPageResponse = redditApiService.getHomePage(after)
        return response.data.children
    }

    override suspend fun getSubredditPage(subreddit: String,after: String): List<SubredditListingModel> {
        var response: SubredditPageResponse = redditApiService.getSubredditPage(subreddit,after)
        return response.data.children
    }
}
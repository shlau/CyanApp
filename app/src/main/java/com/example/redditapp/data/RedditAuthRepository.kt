package com.example.redditapp.data

import com.example.redditapp.network.RedditApiService
import com.example.redditapp.ui.model.AccessResponse
import com.example.redditapp.ui.model.CommentsModel
import com.example.redditapp.ui.model.SubredditPageDataModel
import com.example.redditapp.ui.model.SubredditPageResponse
import com.example.redditapp.ui.model.SubredditsDataModel
import com.example.redditapp.ui.model.SubredditsResponse
import javax.inject.Inject

interface RedditAuthRepository {
    suspend fun getSubscribedSubreddits(): SubredditsDataModel
    suspend fun getHomePage(after: String): SubredditPageDataModel
    suspend fun getSubredditPage(subreddit: String, after: String): SubredditPageDataModel
    suspend fun getAccessToken(code: String, authorization: String): AccessResponse
    suspend fun getComments(url: String): List<CommentsModel>
    suspend fun refreshAccessToken(authorization: String, refreshToken: String): AccessResponse
}

class RedditAuthRepositoryImp @Inject constructor() :

    RedditAuthRepository {
    @Inject
    lateinit var redditApiService: RedditApiService

    override suspend fun getSubscribedSubreddits(): SubredditsDataModel {
        var response: SubredditsResponse = redditApiService.getSubscribedSubreddits()
        return response.data
    }

    override suspend fun getHomePage(after: String): SubredditPageDataModel {
        var response: SubredditPageResponse = redditApiService.getHomePage(after)
        return response.data
    }

    override suspend fun getSubredditPage(
        subreddit: String,
        after: String
    ): SubredditPageDataModel {
        var response: SubredditPageResponse = redditApiService.getSubredditPage(subreddit, after)
        return response.data
    }

    override suspend fun getAccessToken(code: String, authorization: String): AccessResponse {
        return redditApiService.getAccessToken(code = code, authorization = authorization)
    }

    override suspend fun getComments(url: String): List<CommentsModel> {
        return redditApiService.getComments(url)
    }

    override suspend fun refreshAccessToken(
        authorization: String,
        refreshToken: String
    ): AccessResponse {
        return redditApiService.refreshAccessToken(
            refreshToken = refreshToken,
            authorization = authorization
        )
    }
}
package com.example.redditapp.data

import com.example.redditapp.network.RedditApiService
import com.example.redditapp.ui.model.SubredditsData
import com.example.redditapp.ui.model.SubredditsResponse
import javax.inject.Inject

interface RedditAuthRepository {
    suspend fun getSubscribedSubreddits(): SubredditsData
}

class RedditAuthRepositoryImp @Inject constructor() :

    RedditAuthRepository {
    @Inject
    lateinit var redditApiService: RedditApiService

    override suspend fun getSubscribedSubreddits(): SubredditsData {
        var response: SubredditsResponse = redditApiService.getSubscribedSubreddits()
        return response.data
    }
}
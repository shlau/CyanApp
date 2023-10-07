package com.example.redditapp.data

import com.example.redditapp.network.RedditApiService
import com.example.redditapp.ui.model.Subreddits
import retrofit2.Retrofit
import javax.inject.Inject

interface RedditAuthRepository {
    suspend fun getSubscribedSubreddits(token: String): Subreddits
}

class RedditAuthRepositoryImp @Inject constructor() :

    RedditAuthRepository {
    @Inject
    lateinit var redditApiService: RedditApiService
    override suspend fun getSubscribedSubreddits(token: String): Subreddits =
        redditApiService.getSubscribedSubreddits(token)
}
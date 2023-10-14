package com.example.redditapp.network

import com.example.redditapp.ui.model.AccessResponse
import com.example.redditapp.ui.model.CommentsModel
import com.example.redditapp.ui.model.SubredditPageResponse
import com.example.redditapp.ui.model.SubredditsResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

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

    @GET
    suspend fun getComments(@Url url: String): List<CommentsModel>

    @FormUrlEncoded
    @POST("https://www.reddit.com/api/v1/access_token/")
    suspend fun getAccessToken(
        @Header("Authorization") authorization: String,
        @Field("grant_type") grantType: String = "authorization_code",
        @Field("code") code: String,
        @Field("redirect_uri") redirectUri: String = "cyan://reddit"
    ): AccessResponse

    @FormUrlEncoded
    @POST("https://www.reddit.com/api/v1/access_token/")
    suspend fun refreshAccessToken(
        @Header("Authorization") authorization: String,
        @Field("grant_type") grantType: String = "refresh_token",
        @Field("refresh_token") refreshToken: String
    ): AccessResponse
}

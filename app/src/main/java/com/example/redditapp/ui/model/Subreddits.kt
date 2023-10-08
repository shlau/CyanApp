package com.example.redditapp.ui.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubredditsResponse(val data: SubredditsData)

@Serializable
data class SubredditsData(val children: List<Subreddit>)

@Serializable
data class Subreddit(val data: SubredditData)

@Serializable
data class SubredditData(
    @SerialName(value = "display_name") val displayName: String,
    val url: String
)

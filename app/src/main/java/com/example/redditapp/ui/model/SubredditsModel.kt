package com.example.redditapp.ui.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubredditsResponse(val data: SubredditsDataModel)

@Serializable
data class SubredditsDataModel(val children: List<SubredditModel>)

@Serializable
data class SubredditModel(val data: SubredditDataModel)

@Serializable
data class SubredditDataModel(
    @SerialName(value = "display_name") val displayName: String,
    val url: String
)

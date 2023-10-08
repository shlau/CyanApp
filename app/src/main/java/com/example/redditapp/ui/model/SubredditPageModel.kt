package com.example.redditapp.ui.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubredditPageResponse(val data: SubredditPageDataModel)

@Serializable
data class SubredditPageDataModel(val children: List<SubredditListingModel>)

@Serializable
data class SubredditListingModel(val data: SubredditListingDataModel)

@Serializable
data class SubredditListingDataModel(
    val title: String,
    var thumbnail: String?,
    val url: String,
    @SerialName(value = "is_self") val isSelf: Boolean
)
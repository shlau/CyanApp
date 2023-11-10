package com.example.redditapp.ui.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubredditPageResponse(val data: SubredditPageDataModel)

@Serializable
data class SubredditPageDataModel(val children: List<SubredditListingModel>, val after: String)

@Serializable
data class SubredditListingModel(val data: SubredditListingDataModel)

@Serializable
data class RedditVideoModel(
    val height: Int,
    val width: Int,
    @SerialName(value = "has_audio") val hasAudio: Boolean,
    @SerialName(value = "is_gif") val isGif: Boolean,
    @SerialName(value = "fallback_url") val fallbackUrl: String
)

@Serializable
data class SecureMediaModel(
    @SerialName(value = "reddit_video") val redditVideo: RedditVideoModel?
)

@Serializable
data class GalleryItemModel(@SerialName(value = "media_id") val mediaId: String, val id: Long)

@Serializable
data class GalleryDataModel(val items: List<GalleryItemModel>)

@Serializable
data class SubredditListingDataModel(
    val title: String,
    var thumbnail: String?,
    val url: String,
    val permalink: String,
    @SerialName(value = "url_overridden_by_dest") val destUrl: String?,
    @SerialName(value = "is_self") val isSelf: Boolean,
    @SerialName(value = "num_comments") val numComments: Int,
    @SerialName(value = "secure_media") val secureMedia: SecureMediaModel?,
    @SerialName(value = "gallery_data") val galleryData: GalleryDataModel?
)
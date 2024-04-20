package com.example.redditapp.ui.screens.subreddit

import com.example.redditapp.ui.model.SubredditListingDataModel
import com.example.redditapp.ui.screens.media.viewer.MediaTypes

const val FRONTPAGE = "Frontpage"

data class SubredditPageUiState(
    val listings: List<SubredditListingDataModel> = emptyList(),
    val after: String = "",
    val url: String?,
    val subredditDisplayName: String = FRONTPAGE,
    val audioUrl: String?,
    val mediaUrl: String?,
    val mediaType: MediaTypes?,
    val mediaHeight: Int?,
    val mediaWidth: Int?,
    val gallery: List<String>?,
    val openMediaDialog: Boolean = false
)

package com.example.redditapp.ui.screens.subreddit

import com.example.redditapp.ui.model.SubredditListingDataModel

const val FRONTPAGE = "Frontpage"
data class SubredditPageUiState(
    val listings: List<SubredditListingDataModel> = emptyList(),
    val after: String = "",
    val url: String?,
    val subredditDisplayName: String = FRONTPAGE
)

package com.example.redditapp.ui.screens.comments

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.redditapp.ui.screens.subreddit.SubredditPageViewModel

@Composable
fun CommentsScreen(modifier: Modifier = Modifier) {
    val viewModel: CommentsViewModel = hiltViewModel()
    Text(text="Comments page")
}
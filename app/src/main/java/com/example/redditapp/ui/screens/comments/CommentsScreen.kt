package com.example.redditapp.ui.screens.comments

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.redditapp.ui.model.CommentDataModel
import com.example.redditapp.ui.model.CommentModel
import com.example.redditapp.ui.model.CommentsModel
import com.example.redditapp.ui.screens.subreddit.SubredditPageViewModel

@Composable
fun CommentsScreen(modifier: Modifier = Modifier) {
    val viewModel: CommentsViewModel = hiltViewModel()
    val commentsUiState = viewModel.uiState.collectAsState()
    Column {
        Text(text = "Comments page")
        CommentChain(commentsUiState.value.comments, modifier)
    }
}

@Composable
fun CommentChain(comments: List<CommentModel>, modifier: Modifier = Modifier) {
    val viewModel: CommentsViewModel = hiltViewModel()
    val commentsUiState = viewModel.uiState.collectAsState()
    LazyColumn() {
        items(commentsUiState.value.comments) {
            val commentData: CommentDataModel = it.data
            val body: String? = commentData.body

            if (body != null) {
                Text(
                    body,
                    modifier = Modifier.border(width = 1.dp, color = Color.Black).padding(10.dp)
                )
            }
        }
    }
}
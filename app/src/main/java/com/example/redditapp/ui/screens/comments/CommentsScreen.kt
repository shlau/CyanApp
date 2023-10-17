package com.example.redditapp.ui.screens.comments

import android.text.Html
import android.text.Spannable
import android.text.Spanned
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.redditapp.ui.model.CommentDataModel
import com.example.redditapp.ui.model.CommentModel
import com.example.redditapp.ui.model.CommentsModel
import com.example.redditapp.ui.screens.subreddit.SubredditPageViewModel
import com.google.android.material.textview.MaterialTextView


@Composable
fun CommentsScreen(modifier: Modifier = Modifier) {
    val viewModel: CommentsViewModel = hiltViewModel()
    val commentsUiState = viewModel.uiState.collectAsState()
    val originalPostData = commentsUiState.value.originalPost?.data
    if (originalPostData != null) {
        val postBody: Spanned =
            HtmlCompat.fromHtml(originalPostData.selfText ?: "", HtmlCompat.FROM_HTML_MODE_COMPACT)
        Column {
            Column(modifier = Modifier.padding(10.dp)) {
                Text(text = originalPostData.title ?: "")
                val selfText: String? = originalPostData.selfText
                if (selfText != null) {
                    val postBody: Spanned =
                        HtmlCompat.fromHtml(
                            originalPostData.selfText,
                            HtmlCompat.FROM_HTML_MODE_COMPACT
                        )
                    AndroidView(
                        factory = { it -> TextView(it) },
                        update = { it -> it.text = postBody },
                    )
                }
            }
            CommentChain(commentsUiState.value.comments, modifier)
        }
    }
}

@Composable
fun CommentChain(comments: List<CommentModel>, modifier: Modifier = Modifier) {
    val viewModel: CommentsViewModel = hiltViewModel()
    val commentsUiState = viewModel.uiState.collectAsState()
    LazyColumn() {
        items(commentsUiState.value.comments) {
            val commentData: CommentDataModel = it.data
            val bodyHtml: String? = commentData.body
            val depth: Int = it.depth ?: 0
            if (bodyHtml != null) {
                val commentBody: Spanned =
                    HtmlCompat.fromHtml(bodyHtml, HtmlCompat.FROM_HTML_MODE_COMPACT)
                Box(
                    modifier = Modifier
                        .border(width = 1.dp, color = Color.Black)
                        .padding(start = 10.dp)
                        .padding(start = (depth * 10).dp)
                ) {
                    AndroidView(
                        factory = { it -> TextView(it) },
                        update = { it -> it.text = commentBody },
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                color = commentsUiState.value.commentColors[depth % 7]
                            )
                            .padding(10.dp)
                    )
                }
//                Text(
//                    text = Html.fromHtml(body, Html.FROM_HTML_MODE_LEGACY),
//                    modifier = Modifier
//                        .border(width = 1.dp, color = Color.Black)
//                        .padding(10.dp)
//                        .padding(start = (depth * 10).dp)
//                )
            }
        }
    }
}
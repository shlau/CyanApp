package com.example.redditapp.ui.screens.comments

import androidx.compose.ui.graphics.Color
import com.example.redditapp.ui.model.CommentModel
import java.util.UUID

data class CommentsUiState(
    var originalPost: CommentModel?,
    var comments: List<CommentModel>,
    var expandedComments: Set<String>,
    val commentColors: List<Color> = listOf(
        Color.Red,
        Color.Green,
        Color.Cyan,
        Color.Magenta,
        Color.Blue,
        Color.Yellow,
        Color.White
    )
)

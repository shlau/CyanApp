package com.example.redditapp.ui.screens.comments

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.redditapp.Constants.Companion.OAUTH_BASE_URL
import com.example.redditapp.Constants.Companion.REDDIT_API
import com.example.redditapp.data.RedditAuthRepositoryImp
import com.example.redditapp.data.UserDataRepository
import com.example.redditapp.ui.model.CommentModel
import com.example.redditapp.ui.model.CommentsModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentsViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val authRepository: RedditAuthRepositoryImp,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val initialComments = mutableListOf<CommentModel>()
    private val _uiState =
        MutableStateFlow(
            CommentsUiState(
                comments = mutableListOf<CommentModel>(),
                originalPost = null
            )
        )
    val uiState = _uiState.asStateFlow()
    val url = checkNotNull(savedStateHandle.get<String>("url"))
    val permalink = checkNotNull(savedStateHandle.get<String>("permalink"))

    private fun getComments(url: String) {
        viewModelScope.launch {
            val commentsResponse: List<CommentsModel> = authRepository.getComments(url)
            val originalPost: CommentModel = commentsResponse[0].data.children[0]
            val comments: List<CommentModel> =
                (commentsResponse[1].data.children)
            flattenedComments(comments, 0)
            _uiState.update { currentState ->
                currentState.copy(
                    comments = initialComments,
                    originalPost = originalPost
                )
            }
        }
    }

    private fun flattenedComments(comments: List<CommentModel>, depth: Int) {
        comments.forEach {
            it.depth = depth
            initialComments.add(it)
            val replies = it.data.replies
            if (replies != null) {
                val commentChildren = replies.data.children
                flattenedComments(commentChildren, depth + 1)
            }
        }
    }

    init {
        getComments("$OAUTH_BASE_URL$permalink.json")
    }
}
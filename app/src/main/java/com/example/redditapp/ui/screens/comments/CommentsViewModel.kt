package com.example.redditapp.ui.screens.comments

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.redditapp.Constants.Companion.OAUTH_BASE_URL
import com.example.redditapp.data.RedditAuthRepositoryImp
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
    private val authRepository: RedditAuthRepositoryImp,
) : ViewModel() {
    private val flattenedComments = mutableSetOf<String>()
    private val _uiState =
        MutableStateFlow(
            CommentsUiState(
                comments = listOf<CommentModel>(),
                expandedComments = setOf<String>(),
                originalPost = null
            )
        )
    val uiState = _uiState.asStateFlow()
    fun updatePermalink(permalink: String) {
        _uiState.update { currentState -> currentState.copy(permalink = permalink) }
    }

    fun toggleExpandedComments(commentNode: CommentModel) {
        if (_uiState.value.expandedComments.contains(commentNode.data.id)) {

            _uiState.update { currentState ->
                currentState.copy(
                    expandedComments = currentState.expandedComments subtract setOf(commentNode.data.id!!)
                )
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(
                    expandedComments = currentState.expandedComments union setOf(commentNode.data.id!!)
                )
            }
        }
    }

    fun isExpanded(commentNode: CommentModel): Boolean {
        return _uiState.value.expandedComments.contains(commentNode.data.id)
    }

    fun getComments(url: String) {
        viewModelScope.launch {
            val commentsResponse: List<CommentsModel> = authRepository.getComments(url)
            val originalPost: CommentModel = commentsResponse[0].data.children[0]
            val comments: List<CommentModel> =
                (commentsResponse[1].data.children)
            val expandedComments = getFlattenedComments(comments, 0)
            _uiState.update { currentState ->
                currentState.copy(
                    expandedComments = expandedComments,
                    comments = comments,
                    originalPost = originalPost
                )
            }
        }
    }

    private fun getFlattenedComments(
        comments: List<CommentModel>,
        depth: Int
    ): Set<String> {
        comments.forEach {
            it.depth = depth
            val id: String = it.data.id
            flattenedComments.add(id)
            val replies = it.data.replies
            if (replies != null) {
                val commentChildren = replies.data.children
                getFlattenedComments(commentChildren, depth + 1)
            }
        }

        return flattenedComments
    }
}
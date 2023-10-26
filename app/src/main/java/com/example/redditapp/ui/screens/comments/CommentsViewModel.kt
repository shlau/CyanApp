package com.example.redditapp.ui.screens.comments

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.redditapp.Constants.Companion.OAUTH_BASE_URL
import com.example.redditapp.Constants.Companion.REDDIT_API
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
            val expandedComments = getFlattenedComments(comments)
            _uiState.update { currentState ->
                currentState.copy(
                    expandedComments = expandedComments,
                    comments = comments,
                    originalPost = originalPost
                )
            }
        }
    }

    private suspend fun loadSubComments(commentNode: CommentModel) {
        val parentId = commentNode?.data?.id
        val url: String = ("$OAUTH_BASE_URL${_uiState.value.permalink}${parentId}")
        val commentsResponse: List<CommentsModel> = authRepository.getComments(url)
        val comments: List<CommentModel> =
            (commentsResponse[1].data.children)
        val replies: CommentsModel? = comments[0].data.replies

        commentNode.data = commentNode.data.copy(replies = replies)
        getFlattenedComments(comments)
    }

    private suspend fun loadPostComments(postId: String, loadNode: CommentModel) {
        val nextCommentBatch = loadNode.data.children!!.take(100)
        val remainingComments = loadNode.data.children!!.drop(100)
        val newLoadNodeData = loadNode.data.copy(children = remainingComments)
        val newLoadNode = loadNode.copy(data = newLoadNodeData)
        val res: List<CommentModel> = authRepository.getMoreChildren(
            postId,
            nextCommentBatch.joinToString(","),
            loadNode.data.name!!
        )
        _uiState.update { currentState ->
            currentState.copy(
                comments = _uiState.value.comments.dropLast(
                    1
                ) + res + newLoadNode
            )
        }
        getFlattenedComments(res)
    }

    fun loadMoreComments(commentNode: CommentModel?, loadNode: CommentModel, idx: Int) {
        var parentId: String? = null
        parentId = commentNode?.data?.id ?: loadNode.data.id
        if (parentId != null) {
            viewModelScope.launch {
                try {
                    if (commentNode != null) {
                        loadSubComments(commentNode)
                    } else {
                        val postId: String? = loadNode.data.parentId
                        if (postId != null && postId.split("_")[0] == "t3") {
                            loadPostComments(postId, loadNode)
                        } else {
                            val parentNode: CommentModel = _uiState.value.comments[idx - 1]
                            loadSubComments(parentNode)
                            _uiState.update { currentState ->
                                currentState.copy(
                                    comments = _uiState.value.comments.filter {
                                        it.data.id != loadNode.data.id
                                    }
                                )
                            }
                        }
                    }
                    toggleExpandedComments(loadNode)
                } catch (e: Exception) {
                    Log.d(REDDIT_API, e.toString())
                }
            }
        }
    }

    private fun getFlattenedComments(
        comments: List<CommentModel>,
    ): Set<String> {
        comments.forEach {
            val id: String = it.data.id
            flattenedComments.add(id)
            val replies = it.data.replies
            if (replies != null) {
                val commentChildren = replies.data.children
                getFlattenedComments(commentChildren)
            }
        }

        return flattenedComments
    }

    init {
        Log.d(REDDIT_API, "init comments view model")
    }
}
package com.example.redditapp.ui.screens.subreddit

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.redditapp.Constants.Companion.REDDIT_API
import com.example.redditapp.data.RedditAuthRepositoryImp
import com.example.redditapp.ui.model.SubredditListingModel
import com.example.redditapp.ui.model.SubredditListingDataModel
import com.example.redditapp.ui.model.SubredditPageDataModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubredditPageViewModel @Inject constructor(private val redditAuthRepository: RedditAuthRepositoryImp) :
    ViewModel() {
    private val _uiState = MutableStateFlow(
        SubredditPageUiState(
            url = null,
            mediaType = null,
            audioUrl = null,
            mediaUrl = null
        )
    )
    val uiState = _uiState.asStateFlow()

    fun showMedia(audioUrl: String, mediaUrl: String, mediaType: String) {
        _uiState.update { currentState ->
            currentState.copy(
                audioUrl = audioUrl,
                mediaUrl = mediaUrl,
                mediaType = mediaType,
                openMediaDialog = true
            )
        }
    }

    fun hideMedia() {
        _uiState.update { currentState ->
            currentState.copy(
                audioUrl = null,
                mediaUrl = null,
                mediaType = null,
                openMediaDialog = false
            )
        }
    }

    fun appendNextPage() {
        viewModelScope.launch {
            try {
                val after = _uiState.value.after
                val (listingsData, newAfter) = getPageData(
                    _uiState.value.url,
                    after,
                    _uiState.value.subredditDisplayName
                )
                _uiState.update { currentState ->
                    val listings = currentState.listings.toMutableList()
                    listings.addAll(listingsData)
                    currentState.copy(
                        listings = listings,
                        after = newAfter
                    )
                }
            } catch (e: Exception) {
                Log.d(REDDIT_API, e.toString())
            }
        }
    }

    private suspend fun getPageData(
        url: String?,
        after: String = "",
        subredditDisplayName: String?
    ): Pair<List<SubredditListingDataModel>, String> {
        val page: SubredditPageDataModel =
            if (url == null) {
                _uiState.update { currentState ->
                    currentState.copy(
                        subredditDisplayName = FRONTPAGE
                    )
                }
                redditAuthRepository.getHomePage(after)
            } else {
                _uiState.update { currentState ->
                    currentState.copy(
                        url = url,
                        subredditDisplayName = subredditDisplayName ?: ""
                    )
                }
                redditAuthRepository.getSubredditPage(
                    url.split("/")[2],
                    after
                )
            }
        val subredditListings: List<SubredditListingModel> = page.children
        val listingsData = subredditListings.map {
            var thumbnail = it.data.thumbnail
            if (thumbnail != null) {
                it.data.thumbnail = thumbnail.replace("&amp;", "&")
            }
            it.data
        }
        return Pair(listingsData, page.after)
    }

    fun getPageListings(url: String?, after: String = "", subredditDisplayName: String?) {
        viewModelScope.launch {
            try {
                val (listingsData, newAfter) = getPageData(
                    url,
                    after,
                    subredditDisplayName
                )
                _uiState.update { currentState ->
                    currentState.copy(
                        listings = listingsData,
                        after = newAfter
                    )
                }
            } catch (e: Exception) {
                Log.d(REDDIT_API, e.toString())
            }
        }
    }

    init {
        getPageListings(url = null, subredditDisplayName = null)
    }
}
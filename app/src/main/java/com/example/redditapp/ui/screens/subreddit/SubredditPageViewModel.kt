package com.example.redditapp.ui.screens.subreddit

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

data class SubredditPageUiState(
    val listings: List<SubredditListingDataModel> = emptyList(),
    val after: String = "",
    val url: String?,
    val subredditDisplayName: String = "Frontpage"
)

@HiltViewModel
class SubredditPageViewModel @Inject constructor(private val redditAuthRepository: RedditAuthRepositoryImp) :
    ViewModel() {
    private val _uiState = MutableStateFlow(SubredditPageUiState(url = null))
    val uiState = _uiState.asStateFlow()

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
                Log.d("RedditApi", e.toString())
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
                        subredditDisplayName = "Frontpage"
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
                Log.d("RedditApi", e.toString())
            }
        }
    }

    init {
        getPageListings(url = null, subredditDisplayName = null)
    }
}
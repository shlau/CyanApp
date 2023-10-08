package com.example.redditapp.ui.screens.subreddit

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.redditapp.data.RedditAuthRepositoryImp
import com.example.redditapp.ui.model.SubredditListingModel
import com.example.redditapp.ui.model.SubredditListingDataModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SubredditPageUiState(val listings: List<SubredditListingDataModel> = emptyList())

@HiltViewModel
class SubredditPageViewModel @Inject constructor(private val redditAuthRepository: RedditAuthRepositoryImp) :
    ViewModel() {
    private val _uiState = MutableStateFlow(SubredditPageUiState())
    val uiState = _uiState.asStateFlow()
    fun getPageListings(url: String?, after: String = "") {
        viewModelScope.launch {
            try {
                val subredditListings: List<SubredditListingModel> =
                    if (url == null) redditAuthRepository.getHomePage(after) else redditAuthRepository.getSubredditPage(
                        url.split("/")[2],
                        after
                    )
                val listingsData = subredditListings.map {
                    var thumbnail = it.data.thumbnail
                    if (thumbnail != null) {
                        it.data.thumbnail = thumbnail.replace("&amp;", "&")
                    }
                    it.data
                }
                _uiState.update { currentState -> currentState.copy(listings = listingsData) }
            } catch (e: Exception) {
                Log.d("RedditApi", e.toString())
            }
        }
    }

    init {
        getPageListings(null)
    }
}
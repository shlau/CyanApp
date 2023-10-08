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
    private fun getPageListings(after: String = "") {
        viewModelScope.launch {
            try {
                val subredditListings: List<SubredditListingModel> =
                    redditAuthRepository.getSubredditPage(after)
                val listingsData = subredditListings.map { it.data }
                _uiState.update { currentState -> currentState.copy(listings = listingsData) }
            } catch (e: Exception) {
                Log.d("RedditApi", e.toString())
            }
        }
    }

    init {
        getPageListings()
    }
}
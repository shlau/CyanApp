package com.example.redditapp.ui.screens.sidebar

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.redditapp.data.RedditAuthRepositoryImp
import com.example.redditapp.ui.model.SubredditData
import com.example.redditapp.ui.model.SubredditsData
import com.example.redditapp.ui.screens.RedditAppUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NavDrawerUiState(val subscribedSubreddits: List<SubredditData> = emptyList())

@HiltViewModel
class NavDrawerViewModel @Inject constructor(
    private val redditAuthRepository: RedditAuthRepositoryImp,
) : ViewModel() {
    private val _uiState = MutableStateFlow(NavDrawerUiState())
    val uiState: StateFlow<NavDrawerUiState> = _uiState.asStateFlow()
    fun getMySubreddits() {
        viewModelScope.launch {
            try {
                val subscribedSubredditsData: SubredditsData =
                    redditAuthRepository.getSubscribedSubreddits()
                val subscribed = subscribedSubredditsData.children.map { it.data }
                _uiState.update { currentState -> currentState.copy(subscribedSubreddits = subscribed) }
                Log.d("RedditApi", "multi data $subscribed")
            } catch (e: Exception) {
                Log.d("RedditApi", e.toString())
            }
        }
    }

    init {
        getMySubreddits()
    }
}
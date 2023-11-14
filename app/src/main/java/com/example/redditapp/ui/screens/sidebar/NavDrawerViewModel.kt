package com.example.redditapp.ui.screens.sidebar

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.redditapp.Constants.Companion.REDDIT_API
import com.example.redditapp.data.RedditAuthRepositoryImp
import com.example.redditapp.data.UserDataRepository
import com.example.redditapp.ui.model.SubredditsDataModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NavDrawerViewModel @Inject constructor(
    private val redditAuthRepository: RedditAuthRepositoryImp,
    private val userDataRepository: UserDataRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(NavDrawerUiState())
    val uiState: StateFlow<NavDrawerUiState> = _uiState.asStateFlow()
    private fun getMySubreddits() {
        viewModelScope.launch {
            try {
                val subscribedSubredditsData: SubredditsDataModel =
                    redditAuthRepository.getSubscribedSubreddits()
                val subscribed =
                    subscribedSubredditsData.children.map { it.data }
                        .sortedBy { it.displayName.lowercase() }
                _uiState.update { currentState -> currentState.copy(subscribedSubreddits = subscribed) }
                Log.d(REDDIT_API, "multi data $subscribed")
            } catch (e: Exception) {
                Log.d(REDDIT_API, e.toString())
            }
        }
    }

    fun logOut() {
        viewModelScope.launch {
            userDataRepository.updateRedditUserToken("")
        }
    }

    init {
        getMySubreddits()
    }
}
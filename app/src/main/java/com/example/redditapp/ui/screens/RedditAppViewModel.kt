package com.example.redditapp.ui.screens

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.redditapp.data.RedditAuthRepositoryImp
import com.example.redditapp.data.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RedditAppUiState(
    val token: String = ""
)

@HiltViewModel
class RedditAppViewModel @Inject constructor(
    private val redditAuthRepository: RedditAuthRepositoryImp,
    private val userDataRepository: UserDataRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(RedditAppUiState())
    val uiState: StateFlow<RedditAppUiState> = _uiState.asStateFlow()
    val userTokenFlow: Flow<String> = userDataRepository.userTokenFlow

    fun updateToken(token: String) {
        viewModelScope.launch {
            userDataRepository.updateUserToken(token)
        }
        _uiState.update { currentState -> currentState.copy(token = token) }
    }

    fun getMySubreddits() {
        viewModelScope.launch {
            try {
                val subscribed =
                    redditAuthRepository.getSubscribedSubreddits(uiState.value.token)
                Log.d("RedditApi", "multi data $subscribed")
            } catch (e: Exception) {
                Log.d("RedditApi", e.toString())
            }
        }
    }

}

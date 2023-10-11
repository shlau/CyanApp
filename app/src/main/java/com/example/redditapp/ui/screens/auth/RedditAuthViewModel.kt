package com.example.redditapp.ui.screens.auth

import androidx.compose.ui.platform.UriHandler
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class RedditAuthUiState(
    val apiKey: String = "",
)

class RedditAuthViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(RedditAuthUiState())
    val uiState: StateFlow<RedditAuthUiState> = _uiState.asStateFlow()

    fun updateApiKey(key: String) {
        _uiState.update { currentState -> currentState.copy(apiKey = key) }
    }

    fun openRedditLogin(uriHandler: UriHandler) {
        val clientId = _uiState.value.apiKey
        val scope =
            "identity edit flair history modconfig modflair modlog modposts modwiki mysubreddits privatemessages read report save submit subscribe vote wikiedit wikiread"
        val redirectUri = "cyan://reddit"
        val url =
            "https://www.reddit.com/api/v1/authorize?client_id=$clientId&response_type=code&state=$clientId&redirect_uri=$redirectUri&duration=permanent&scope=$scope"
        uriHandler.openUri(url)
    }
}
package com.example.redditapp.ui.screens.auth.imgur

import androidx.compose.ui.platform.UriHandler
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.redditapp.data.UserDataRepository
import com.example.redditapp.ui.screens.auth.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImgurAuthViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
) : AuthService, ViewModel() {
    private val _uiState = MutableStateFlow(ImgurAuthUiState())
    val uiState: StateFlow<ImgurAuthUiState> = _uiState.asStateFlow()
    override fun updateApiKey(key: String) {
        _uiState.update { currentState -> currentState.copy(apiKey = key) }
    }

    fun saveApiKey() {
        viewModelScope.launch {
            userDataRepository.updateImgurClientId(uiState.value.apiKey)
        }
    }
}
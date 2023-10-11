package com.example.redditapp.ui.screens

import android.net.Credentials
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.redditapp.data.RedditAuthRepositoryImp
import com.example.redditapp.data.UserDataRepository
import com.example.redditapp.ui.model.AccessResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RedditAppUiState(
    var code: String = ""
)

@HiltViewModel
class RedditAppViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val authRepository: RedditAuthRepositoryImp
) : ViewModel() {
    private val _uiState = MutableStateFlow(RedditAppUiState())
    private val refreshTokenFlow: Flow<String> = userDataRepository.refreshTokenFlow
    val uiState: StateFlow<RedditAppUiState> = _uiState.asStateFlow()
    val userTokenFlow: Flow<String> = userDataRepository.userTokenFlow
    val tokenExpirationFlow: Flow<String> = userDataRepository.tokenExpirationFlow
    val clientIdFlow: Flow<String> = userDataRepository.clientIdFlow

    fun updateCode(code: String) {
        _uiState.value.code = code
    }

    fun getParamMapping(queryParamStr: String): HashMap<String, String> {
        val mapping = HashMap<String, String>()
        val params = queryParamStr.split("&")

        params.forEach {
            val parts = it.split("=")
            val paramKey = parts[0]
            val paramVal = parts[1]
            mapping[paramKey] = paramVal
        }

        return mapping
    }

    fun getAccessResponse(code: String, clientId: String) {
        viewModelScope.launch {
            try {
                val clientSecret = ""
                val credentials: String = okhttp3.Credentials.basic(clientId, clientSecret)
                val res =
                    authRepository.getAccessToken(code = code, authorization = credentials)
                userDataRepository.updateUserToken("bearer ${res.accessToken}")
                userDataRepository.updateRefreshToken(res.refreshToken)
                userDataRepository.updateTokenExpiration(res.expiresIn.toString())
                userDataRepository.updateClientId(clientId)
                Log.d("RedditApi", "auth res: ${res.toString()}")
            } catch (e: Exception) {
                Log.d("RedditApi", e.toString())
            }
        }
    }

    fun refreshAccessToken() {
        viewModelScope.launch {
            try {
                val clientId = clientIdFlow.first()
                val clientSecret = ""
                val credentials: String = okhttp3.Credentials.basic(clientId, clientSecret)
                val res =
                    authRepository.refreshAccessToken(refreshToken = refreshTokenFlow.first(), authorization = credentials)
                userDataRepository.updateUserToken("bearer ${res.accessToken}")
                userDataRepository.updateRefreshToken(res.refreshToken)
            }
            catch (e: Exception) {
                Log.d("RedditApi", e.toString())
            }
        }
    }

    fun clearUserData() {
        viewModelScope.launch {
            userDataRepository.clearUserDataStore()
        }
    }
}

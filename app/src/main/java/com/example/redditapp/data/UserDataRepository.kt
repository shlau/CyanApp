package com.example.redditapp.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserDataRepository @Inject constructor(private val userDataStore: DataStore<Preferences>) {
    object PreferenceKeys {
        val IMGUR_CLIENT_ID = stringPreferencesKey("imgur_client_id")
        val REDDIT_CLIENT_ID = stringPreferencesKey("reddit_client_id")
        val REDDIT_USER_TOKEN = stringPreferencesKey("reddit_user_token")
        val REDDIT_REFRESH_TOKEN = stringPreferencesKey("reddit_refresh_token")
        val REDDIT_TOKEN_EXPIRATION = longPreferencesKey("reddit_token_expiration")
        val REDDIT_TOKEN_TIMESTAMP = longPreferencesKey("reddit_token_timestamp")
    }

    val redditUserTokenFlow: Flow<String> = userDataStore.data.map { currentPreferences ->
        currentPreferences[PreferenceKeys.REDDIT_USER_TOKEN] ?: ""
    }
    val refreshRedditTokenFlow: Flow<String> = userDataStore.data.map { currentPreferences ->
        currentPreferences[PreferenceKeys.REDDIT_REFRESH_TOKEN] ?: ""
    }
    val redditTokenExpirationFlow: Flow<Long> = userDataStore.data.map { currentPreferences ->
        currentPreferences[PreferenceKeys.REDDIT_TOKEN_EXPIRATION] ?: -1
    }

    val redditTokenTimestampFlow: Flow<Long> = userDataStore.data.map { currentPreferences ->
        currentPreferences[PreferenceKeys.REDDIT_TOKEN_TIMESTAMP] ?: -1
    }

    val redditClientIdFlow: Flow<String> = userDataStore.data.map { currentPreferences ->
        currentPreferences[PreferenceKeys.REDDIT_CLIENT_ID] ?: ""
    }

    val imgurClientIdFlow: Flow<String> = userDataStore.data.map { currentPreferences ->
        currentPreferences[PreferenceKeys.IMGUR_CLIENT_ID] ?: ""
    }

    suspend fun updateRedditUserToken(token: String) {
        userDataStore.edit { currentSettings ->
            currentSettings[PreferenceKeys.REDDIT_USER_TOKEN] = token
        }
    }

    suspend fun updateRedditRefreshToken(token: String) {
        userDataStore.edit { currentSettings ->
            currentSettings[PreferenceKeys.REDDIT_REFRESH_TOKEN] = token
        }
    }

    suspend fun updateRedditTokenExpiration(expiration: Long) {
        userDataStore.edit { currentSettings ->
            currentSettings[PreferenceKeys.REDDIT_TOKEN_EXPIRATION] = expiration
        }
    }

    suspend fun updateRedditTokenTimestamp(timestamp: Long) {
        userDataStore.edit { currentSettings ->
            currentSettings[PreferenceKeys.REDDIT_TOKEN_TIMESTAMP] = timestamp
        }
    }

    suspend fun updateRedditClientId(clientId: String) {
        userDataStore.edit { currentSettings ->
            currentSettings[PreferenceKeys.REDDIT_CLIENT_ID] = clientId
        }
    }

    suspend fun updateImgurClientId(clientId: String) {
        userDataStore.edit { currentSettings ->
            currentSettings[PreferenceKeys.IMGUR_CLIENT_ID] = clientId
        }
    }

    suspend fun clearUserDataStore() {
        userDataStore.edit { it.clear() }
    }

    fun getRedditToken() =
        userDataStore.data.map { it[PreferenceKeys.REDDIT_USER_TOKEN] }

    fun getRedditRefreshToken() =
        userDataStore.data.map { it[PreferenceKeys.REDDIT_REFRESH_TOKEN] }

    fun getRedditTokenExpiration() =
        userDataStore.data.map { it[PreferenceKeys.REDDIT_TOKEN_EXPIRATION] }

    fun getRedditTokenTimestamp() =
        userDataStore.data.map { it[PreferenceKeys.REDDIT_TOKEN_TIMESTAMP] }

    fun getRedditClientId() =
        userDataStore.data.map { it[PreferenceKeys.REDDIT_CLIENT_ID] }

    fun getImgurClientId() =
        userDataStore.data.map { it[PreferenceKeys.IMGUR_CLIENT_ID] }
}
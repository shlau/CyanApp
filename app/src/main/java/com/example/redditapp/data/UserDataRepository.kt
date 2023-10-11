package com.example.redditapp.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserDataRepository @Inject constructor(private val userDataStore: DataStore<Preferences>) {
    val USER_TOKEN = stringPreferencesKey("user_token")
    val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    val TOKEN_EXPIRATION = stringPreferencesKey("token_expiration")
    val CLIENT_ID = stringPreferencesKey("client_id")
    val userTokenFlow: Flow<String> = userDataStore.data.map { currentPreferences ->
        currentPreferences[USER_TOKEN] ?: ""
    }
    val refreshTokenFlow: Flow<String> = userDataStore.data.map { currentPreferences ->
        currentPreferences[REFRESH_TOKEN] ?: ""
    }
    val tokenExpirationFlow: Flow<String> = userDataStore.data.map { currentPreferences ->
        currentPreferences[TOKEN_EXPIRATION] ?: ""
    }

    val clientIdFlow: Flow<String> = userDataStore.data.map { currentPreferences ->
        currentPreferences[CLIENT_ID] ?: ""
    }

    suspend fun updateUserToken(token: String) {
        userDataStore.edit { currentSettings ->
            currentSettings[USER_TOKEN] = token
        }
    }

    suspend fun updateRefreshToken(token: String) {
        userDataStore.edit { currentSettings ->
            currentSettings[REFRESH_TOKEN] = token
        }
    }

    suspend fun updateTokenExpiration(expiration: String) {
        userDataStore.edit { currentSettings ->
            currentSettings[TOKEN_EXPIRATION] = expiration
        }
    }

    suspend fun updateClientId(clientId: String) {
        userDataStore.edit { currentSettings ->
            currentSettings[CLIENT_ID] = clientId
        }
    }

    suspend fun clearUserDataStore() {
        userDataStore.edit { it.clear() }
    }

    fun getToken() =
        userDataStore.data.map { it[USER_TOKEN] }

    fun getRefreshToken() =
        userDataStore.data.map { it[REFRESH_TOKEN] }

    fun getTokenExpiration() =
        userDataStore.data.map { it[TOKEN_EXPIRATION] }
    fun getClientId() =
        userDataStore.data.map { it[CLIENT_ID] }
}
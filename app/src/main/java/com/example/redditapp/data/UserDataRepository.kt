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
        val CLIENT_ID = stringPreferencesKey("client_id")
        val USER_TOKEN = stringPreferencesKey("user_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        val TOKEN_EXPIRATION = longPreferencesKey("token_expiration")
        val TOKEN_TIMESTAMP = longPreferencesKey("token_timestamp")
    }

    val userTokenFlow: Flow<String> = userDataStore.data.map { currentPreferences ->
        currentPreferences[PreferenceKeys.USER_TOKEN] ?: ""
    }
    val refreshTokenFlow: Flow<String> = userDataStore.data.map { currentPreferences ->
        currentPreferences[PreferenceKeys.REFRESH_TOKEN] ?: ""
    }
    val tokenExpirationFlow: Flow<Long> = userDataStore.data.map { currentPreferences ->
        currentPreferences[PreferenceKeys.TOKEN_EXPIRATION] ?: -1
    }

    val tokenTimestampFlow: Flow<Long> = userDataStore.data.map { currentPreferences ->
        currentPreferences[PreferenceKeys.TOKEN_TIMESTAMP] ?: -1
    }


    val clientIdFlow: Flow<String> = userDataStore.data.map { currentPreferences ->
        currentPreferences[PreferenceKeys.CLIENT_ID] ?: ""
    }

    suspend fun updateUserToken(token: String) {
        userDataStore.edit { currentSettings ->
            currentSettings[PreferenceKeys.USER_TOKEN] = token
        }
    }

    suspend fun updateRefreshToken(token: String) {
        userDataStore.edit { currentSettings ->
            currentSettings[PreferenceKeys.REFRESH_TOKEN] = token
        }
    }

    suspend fun updateTokenExpiration(expiration: Long) {
        userDataStore.edit { currentSettings ->
            currentSettings[PreferenceKeys.TOKEN_EXPIRATION] = expiration
        }
    }

    suspend fun updateTokenTimestamp(timestamp: Long) {
        userDataStore.edit { currentSettings ->
            currentSettings[PreferenceKeys.TOKEN_TIMESTAMP] = timestamp
        }
    }
    suspend fun updateClientId(clientId: String) {
        userDataStore.edit { currentSettings ->
            currentSettings[PreferenceKeys.CLIENT_ID] = clientId
        }
    }

    suspend fun clearUserDataStore() {
        userDataStore.edit { it.clear() }
    }

    fun getToken() =
        userDataStore.data.map { it[PreferenceKeys.USER_TOKEN] }

    fun getRefreshToken() =
        userDataStore.data.map { it[PreferenceKeys.REFRESH_TOKEN] }

    fun getTokenExpiration() =
        userDataStore.data.map { it[PreferenceKeys.TOKEN_EXPIRATION] }

    fun getTokenTimestamp() =
        userDataStore.data.map { it[PreferenceKeys.TOKEN_TIMESTAMP] }

    fun getClientId() =
        userDataStore.data.map { it[PreferenceKeys.CLIENT_ID] }
}
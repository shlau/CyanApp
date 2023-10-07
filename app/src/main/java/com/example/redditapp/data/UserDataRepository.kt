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
    val userTokenFlow: Flow<String> = userDataStore.data.map { currentPreferences ->
        currentPreferences[USER_TOKEN] ?: ""
    }

    suspend fun updateUserToken(token: String) {
        userDataStore.edit { currentSettings ->
            currentSettings[USER_TOKEN] = token
        }
    }
}
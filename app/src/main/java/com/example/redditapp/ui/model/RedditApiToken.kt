package com.example.redditapp.ui.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RedditApiToken(
    @SerialName(value = "access_token") val accessToken: String,
    @SerialName(value = "token_type") val tokenType: String,
    @SerialName(value = "expires_in") val expiresIn: String,
    @SerialName(value = "refresh_token") val refreshToken: String,
    val scope: String,
)

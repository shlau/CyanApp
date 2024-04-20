package com.example.redditapp.ui.screens.media.viewer

import kotlinx.serialization.Serializable

enum class MediaTypes {
    VIDEO,
    IMAGE
}
@Serializable
data class MediaViewerModel(
    val mediaUrl: String? = null,
    val audioUrl: String? = null,
    val mediaType: MediaTypes? = null,
    val gallery: List<String>? = null,
    val height: Int? = null,
    val width: Int? = null,
)

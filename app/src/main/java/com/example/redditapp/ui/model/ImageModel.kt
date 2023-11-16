package com.example.redditapp.ui.model

import kotlinx.serialization.Serializable

@Serializable
data class ImageModel(
    val id: String,
    val title: String?,
    val width: Int,
    val height: Int,
    val type: String,
    val animated: Boolean,
    val link: String
)

@Serializable
data class ImgurAlbumModel(
    val data: List<ImageModel>,
)

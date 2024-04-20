package com.example.redditapp.util

import android.util.Log
import com.example.redditapp.Constants
import com.example.redditapp.ui.model.GalleryDataModel
import com.example.redditapp.ui.model.ImageModel
import com.example.redditapp.ui.model.RedditVideoModel
import com.example.redditapp.ui.screens.media.viewer.MediaTypes
import com.example.redditapp.ui.screens.media.viewer.MediaViewerModel
import java.net.URL
import java.util.Objects

object Util {
    private fun getFileExtension(url: URL): String? {
        Objects.requireNonNull(url, "URL is null")
        val file = url.file
        if (file.contains(".")) {
            val sub = file.substring(file.lastIndexOf('.') + 1)
            if (sub.isEmpty()) {
                return null
            }
            if (sub.contains("?")) {
                return (sub.substring(0, sub.indexOf('?')))
            }
            if (sub.contains("#")) {
                return (sub.substring(0, sub.indexOf('#')))
            }
            return sub
        }
        return null
    }

    private fun getMediaType(urlString: String?): MediaTypes? {
        if (urlString == null) {
            return null
        }
        val url = URL(urlString)
        val fileType = getFileExtension(url)
        if (fileType != null) {
            val videoTypes = listOf("mp4", "gifv")
            val imageTypes = listOf("jpg", "jpeg", "png", "gif")
            if (videoTypes.contains(fileType)) {
                return MediaTypes.VIDEO
            }
            if (imageTypes.contains(fileType)) {
                return MediaTypes.IMAGE
            }
        }
        return null
    }

    suspend fun getMediaData(
        redditVideo: RedditVideoModel? = null,
        url: String? = null,
        galleryData: GalleryDataModel? = null,
        getAlbumImages: suspend (albumId: String) -> List<ImageModel>
    ): MediaViewerModel? {
        if (redditVideo != null) {
            val baseUrl = redditVideo.fallbackUrl.split("_")[0]
            val audioUrl = if (redditVideo.hasAudio) "${baseUrl}_AUDIO_128.mp4" else null

            return MediaViewerModel(
                audioUrl = audioUrl,
                mediaUrl = redditVideo.fallbackUrl,
                mediaType = MediaTypes.VIDEO,
                height = redditVideo.height,
                width = redditVideo.width
            )
        }
        else if (galleryData != null) {
            val baseImageUrl = "https://i.redd.it/"
            val gallery =
                galleryData.items.map { "${baseImageUrl}${it.mediaId}.jpg" }
            return MediaViewerModel(
                gallery = gallery,
                mediaType = MediaTypes.IMAGE,
            )
        }
        else if (url != null) {
            val mediaType = getMediaType(url)
            if (mediaType != null) {
                return MediaViewerModel(
                    mediaUrl = url.replace("gifv", "mp4"),
                    mediaType = mediaType,
                )
            }
            else if (url.contains("imgur.com")) {
                val url = URL(url)
                val albumUrlRegex = Regex("/a/([a-zA-Z0-9]+)$")
                val match = albumUrlRegex.find(url.file)
                if (match != null) {
                    val (albumId) = match.destructured
                    return try {
                        val images = getAlbumImages(albumId)
                        val gallery = images.map { it.link }
                        MediaViewerModel(
                            gallery = gallery,
                            mediaType = MediaTypes.IMAGE,
                        )
                    } catch (e: Exception) {
                        Log.d(Constants.REDDIT_API, e.toString())
                        null
                    }

                }
            }
        }
        return null
    }
}
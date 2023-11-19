package com.example.redditapp.ui.screens.subreddit

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.redditapp.Constants.Companion.REDDIT_API
import com.example.redditapp.data.ImgurAuthRepositoryImp
import com.example.redditapp.data.RedditAuthRepositoryImp
import com.example.redditapp.ui.model.RedditVideoModel
import com.example.redditapp.ui.model.SubredditListingDataModel
import com.example.redditapp.ui.model.SubredditListingModel
import com.example.redditapp.ui.model.SubredditPageDataModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.URL
import java.util.Objects
import javax.inject.Inject


@HiltViewModel
class SubredditPageViewModel @Inject constructor(
    private val redditAuthRepository: RedditAuthRepositoryImp,
    private val imgurAuthRepository: ImgurAuthRepositoryImp
) :
    ViewModel() {
    private val _uiState = MutableStateFlow(
        SubredditPageUiState(
            url = null,
            mediaType = null,
            audioUrl = null,
            mediaUrl = null,
            mediaHeight = null,
            mediaWidth = null,
            gallery = null,
        )
    )
    val uiState = _uiState.asStateFlow()

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

    private fun getMediaType(urlString: String): String? {
        val url = URL(urlString)
        val fileType = getFileExtension(url)
        if (fileType != null) {
            val videoTypes = listOf("mp4", "gifv")
            val imageTypes = listOf("jpg", "jpeg", "png", "gif")
            if (videoTypes.contains(fileType)) {
                return "video"
            }
            if (imageTypes.contains(fileType)) {
                return "image"
            }
        }
        return null
    }

    fun showMedia(listing: SubredditListingDataModel) {
        val video: RedditVideoModel? = listing.secureMedia?.redditVideo
        if (video != null) {
            val baseUrl = video.fallbackUrl.split("_")[0]
            val audioUrl = if (video.hasAudio) "${baseUrl}_AUDIO_128.mp4" else null
            _uiState.update { currentState ->
                currentState.copy(
                    audioUrl = audioUrl,
                    mediaUrl = video.fallbackUrl,
                    mediaType = "video",
                    openMediaDialog = true,
                    mediaHeight = video.height,
                    mediaWidth = video.width,
                )
            }
        } else {
            val mediaType = getMediaType(listing.url)
            if (false && mediaType != null) {
                _uiState.update { currentState ->
                    currentState.copy(
                        mediaUrl = listing.url.replace("gifv", "mp4"),
                        mediaType = mediaType,
                        openMediaDialog = true
                    )
                }
            } else if (false && listing.galleryData != null) {
                val baseImageUrl = "https://i.redd.it/"
                val gallery =
                    listing.galleryData.items.map { "${baseImageUrl}${it.mediaId}.jpg" }
                _uiState.update { currentState ->
                    currentState.copy(
                        gallery = gallery,
                        mediaUrl = null,
                        mediaType = "image",
                        openMediaDialog = true,
                    )
                }
            } else if (listing.url.contains("imgur.com")) {
                val url = URL(listing.url)
                val albumUrlRegex = Regex("/a/([a-zA-Z0-9]+)$")
                val match = albumUrlRegex.find(url.file)
                if (match != null) {
                    val (albumId) = match.destructured
                    viewModelScope.launch {
                        try {
                            val images = imgurAuthRepository.getAlbumImages(albumId)
                            val gallery = images.map { it.link }
                            _uiState.update { currentState ->
                                currentState.copy(
                                    gallery = gallery,
                                    mediaUrl = null,
                                    mediaType = "image",
                                    openMediaDialog = true,
                                )
                            }
                        } catch (e: Exception) {
                            Log.d(REDDIT_API, e.toString())
                        }
                    }

                }
            }
        }
    }

    fun hideMedia() {
        _uiState.update { currentState ->
            currentState.copy(
                gallery = null,
                audioUrl = null,
                mediaUrl = null,
                mediaType = null,
                openMediaDialog = false
            )
        }
    }

    fun appendNextPage() {
        viewModelScope.launch {
            try {
                val after = _uiState.value.after
                val (listingsData, newAfter) = getPageData(
                    _uiState.value.url,
                    after,
                    _uiState.value.subredditDisplayName
                )
                _uiState.update { currentState ->
                    val listings = currentState.listings.toMutableList()
                    listings.addAll(listingsData)
                    currentState.copy(
                        listings = listings,
                        after = newAfter
                    )
                }
            } catch (e: Exception) {
                Log.d(REDDIT_API, e.toString())
            }
        }
    }

    private suspend fun getPageData(
        url: String?,
        after: String = "",
        subredditDisplayName: String?
    ): Pair<List<SubredditListingDataModel>, String> {
        val page: SubredditPageDataModel =
            if (url == null) {
                _uiState.update { currentState ->
                    currentState.copy(
                        subredditDisplayName = FRONTPAGE
                    )
                }
                redditAuthRepository.getHomePage(after)
            } else {
                _uiState.update { currentState ->
                    currentState.copy(
                        url = url,
                        subredditDisplayName = subredditDisplayName ?: ""
                    )
                }
                redditAuthRepository.getSubredditPage(
                    url.split("/")[2],
                    after
                )
            }
        val subredditListings: List<SubredditListingModel> = page.children
        val listingsData = subredditListings.map {
            var thumbnail = it.data.thumbnail
            if (thumbnail != null) {
                it.data.thumbnail = thumbnail.replace("&amp;", "&")
            }
            it.data
        }
        return Pair(listingsData, page.after)
    }

    fun getPageListings(url: String?, after: String = "", subredditDisplayName: String?) {
        viewModelScope.launch {
            try {
                val (listingsData, newAfter) = getPageData(
                    url,
                    after,
                    subredditDisplayName
                )
                _uiState.update { currentState ->
                    currentState.copy(
                        listings = listingsData,
                        after = newAfter
                    )
                }
            } catch (e: Exception) {
                Log.d(REDDIT_API, e.toString())
            }
        }
    }

    init {
        getPageListings(url = null, subredditDisplayName = null)
    }
}
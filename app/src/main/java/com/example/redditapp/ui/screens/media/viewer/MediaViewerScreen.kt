package com.example.redditapp.ui.screens.media.viewer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.media3.common.MediaItem.fromUri
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.MergingMediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.request.ImageRequest

@Composable
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
fun MediaViewerScreen(
    onDismissRequest: () -> Unit,
    mediaUrl: String?,
    audioUrl: String?,
    mediaType: String?,
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
        ) {

            if (mediaUrl != null) {
                if (mediaType == "image") {
                    val imageLoader = ImageLoader.Builder(LocalContext.current)
                        .components { add(GifDecoder.Factory()) }.build()
                    Image(
                        painter = rememberAsyncImagePainter(
                            ImageRequest.Builder(LocalContext.current)
                                .data(data = mediaUrl)
                                .build(), imageLoader = imageLoader
                        ),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                    )
                }
                if (mediaType == "video") {
                    val context = LocalContext.current
                    val dataSourceFactory = DefaultHttpDataSource.Factory()
                    val videoSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(fromUri(mediaUrl))
                    var mediaSrc: MediaSource = videoSource
                    if (audioUrl != null) {
                        val audioSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                            .createMediaSource(fromUri(audioUrl))
                        mediaSrc = MergingMediaSource(videoSource, audioSource)
                    }
                    val viewPlayer = ExoPlayer.Builder(context).build().apply {
                        setMediaSource(mediaSrc)
                        playWhenReady = true
                        prepare()
                    }
                    AndroidView(
                        factory = {
                            PlayerView(context).apply {
                                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                                player = viewPlayer
                            }
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun MediaViewerScreenPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Gray)
    ) {
        MediaViewerScreen(
            {},
            "https://v.redd.it/jk90e6z0yb1/DASH_1080.mp4?source=fallback",
            "https://v.redd.it/jo2fyf21dpxb1/DASH_AUDIO_128.mp4",
            "video"
        )
    }
}
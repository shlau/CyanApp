package com.example.redditapp.ui.screens.media.viewer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.media3.common.MediaItem.fromUri
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView

@Composable
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
fun MediaViewerScreen(
    onDismissRequest: () -> Unit,
    mediaUrl: String,
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            val context = LocalContext.current
            val viewPlayer = ExoPlayer.Builder(context).build().apply {
                setMediaItem(fromUri(mediaUrl))
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

@Preview
@Composable
fun MediaViewerScreenPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Gray)
    ) {
        MediaViewerScreen(
            {}, "https://v.redd.it/jk90e6z0yb1/DASH_1080.mp4?source=fallback"
        )
    }
}
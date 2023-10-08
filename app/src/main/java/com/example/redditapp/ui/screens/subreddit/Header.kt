package com.example.redditapp.ui.screens.subreddit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun Header(subredditName: String, modifier: Modifier = Modifier) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .background(color = Color.Gray)) {
        Text(
            text = subredditName, modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 20.dp, start = 20.dp
                )
                .height(40.dp)
        )
    }
}

//@Preview
@Composable
fun headerPreview() {
    Box(modifier = Modifier.fillMaxSize()) {
        Header("Frontpage")
    }
}
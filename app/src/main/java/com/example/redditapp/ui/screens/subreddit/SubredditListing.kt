package com.example.redditapp.ui.screens.subreddit

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.redditapp.R
import com.example.redditapp.ui.model.SubredditListingModel
import com.example.redditapp.ui.model.SubredditListingDataModel

@Composable
fun SubredditListing(listing: SubredditListingDataModel, modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = Color.Black)
            .defaultMinSize(minHeight = 60.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = listing.title,
            modifier = Modifier
                .padding(10.dp)
                .weight(1.0f),
            textAlign = TextAlign.Justify
        )
        if (!listing.isSelf && listing.thumbnail != null && listing.thumbnail != "default") {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(listing.thumbnail).crossfade(true).build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.loading_image),
                error = painterResource(id = R.drawable.broken_image),
                modifier = Modifier
                    .height(60.dp)
                    .width(60.dp)
            )
        }
    }
}

//@Preview
@Composable
fun listingPreview() {
    val l = SubredditListingDataModel(
        isSelf = false,
        thumbnail = "",
        title = "Palestinians tearing down Israeli fence which kept them inside",
        url = "test"
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        SubredditListing(listing = l)
    }
}
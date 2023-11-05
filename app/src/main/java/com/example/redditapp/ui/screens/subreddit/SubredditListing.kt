package com.example.redditapp.ui.screens.subreddit

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.request.ImageRequest
import com.example.redditapp.Constants.Companion.REDDIT_API
import com.example.redditapp.R
import com.example.redditapp.ui.model.RedditVideoModel
import com.example.redditapp.ui.model.SubredditListingModel
import com.example.redditapp.ui.model.SubredditListingDataModel
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection

@Composable
fun SubredditListing(
    permalink: String,
    url: String,
    navToComments: (String, String) -> Unit,
    listing: SubredditListingDataModel, modifier: Modifier = Modifier,
    showMedia: (listing: SubredditListingDataModel) -> Unit
) {
    val localUriHandler = LocalUriHandler.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = Color.Black)
            .defaultMinSize(minHeight = 60.dp)
            .clickable {
                if (!listing.isSelf && listing.destUrl != null) {
                    localUriHandler.openUri(listing.destUrl)
                } else {
                    navToComments(url, permalink)
                }
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .weight(1.0f),
        ) {
            Text(
                text = listing.title,
                textAlign = TextAlign.Justify
            )
            Text(
                text = "${listing.numComments} comments",
                color = Color.Blue,
                modifier = Modifier.clickable { navToComments(url, permalink) },
            )
        }
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
                    .clickable {
                        showMedia(listing)
                    }
            )
        }
    }
}

//@Preview
@Composable
fun listingPreview(
) {
//    val l = SubredditListingDataModel(
//        isSelf = false,
//        thumbnail = "",
//        title = "Palestinians tearing down Israeli fence which kept them inside",
//        url = "test",
//        permalink = "testing perma",
//        numComments = 55,
//        destUrl = null
//    )
//    SubredditListing(
//        permalink = l.permalink,
//        url = l.url,
//        navToComments = { s: String, s2: String -> true },
//        listing = l
//    )
}
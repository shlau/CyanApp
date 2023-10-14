package com.example.redditapp.ui.screens.subreddit

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.redditapp.R
import com.example.redditapp.ui.model.SubredditListingDataModel
import com.example.redditapp.ui.screens.sidebar.NavDrawer
import kotlinx.coroutines.launch

@Composable
fun SubredditListings(
    navToComments: (String, String) -> Unit,
    listings: List<SubredditListingDataModel>,
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val isAtBottom = !listState.canScrollForward
    val viewModel: SubredditPageViewModel = hiltViewModel()
    if (isAtBottom) {
        LaunchedEffect(Unit) {
            if (listings.isNotEmpty()) {
                viewModel.appendNextPage()
            }
        }
    }
    LazyColumn(state = listState, modifier = Modifier.padding(innerPadding)) {
        items(listings) {
            SubredditListing(
                permalink = it.permalink,
                url = it.url,
                navToComments = navToComments,
                listing = it
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubredditPage(navToComments: (String, String) -> Unit, modifier: Modifier = Modifier) {
    var drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val viewModel: SubredditPageViewModel = hiltViewModel()
    val subredditPageUiState = viewModel.uiState.collectAsState()

    ModalNavigationDrawer(drawerState = drawerState, drawerContent = {
        ModalDrawerSheet {
            NavDrawer(onSubredditClick = { url: String?, subredditDisplayName: String? ->
                viewModel.getPageListings(url = url, subredditDisplayName = subredditDisplayName)
                scope.launch {
                    drawerState.apply { close() }
                }
            })
        }
    }) {
        Scaffold(topBar = { Header(subredditPageUiState.value.subredditDisplayName) }, bottomBar = {
            Footer({
                scope.launch {
                    drawerState.apply {
                        open()
                    }
                }
            })
        }) { innerPadding ->
            SubredditListings(
                navToComments = navToComments,
                innerPadding = innerPadding,
                listings = subredditPageUiState.value.listings
            )
        }
    }
}


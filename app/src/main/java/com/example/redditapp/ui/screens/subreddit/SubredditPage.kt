package com.example.redditapp.ui.screens.subreddit

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.redditapp.R
import com.example.redditapp.ui.screens.sidebar.NavDrawer
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun SubredditPage(modifier: Modifier = Modifier) {
    var drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val viewModel: SubredditPageViewModel = hiltViewModel()
    val subredditPageUiState = viewModel.uiState.collectAsState()
    ModalNavigationDrawer(drawerState = drawerState, drawerContent = {
        ModalDrawerSheet {
            NavDrawer(onSubredditClick = { url: String? ->
                viewModel.getPageListings(url)
                scope.launch {
                    drawerState.apply { close() }
                }
            })
        }
    }) {
        Scaffold(bottomBar = {
            Footer({
                scope.launch {
                    drawerState.apply {
                        open()
                    }
                }
            })
        }) { innerPadding ->
            LazyColumn(modifier = Modifier.padding(innerPadding)) {
                items(subredditPageUiState.value.listings) {
                    SubredditListing(listing = it)
                }
            }
        }
    }
}


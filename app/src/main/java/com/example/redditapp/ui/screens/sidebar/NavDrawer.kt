package com.example.redditapp.ui.screens.sidebar

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.redditapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavDrawer(onSubredditClick: (String?, String?) -> Unit, modifier: Modifier = Modifier) {
    val viewModel: NavDrawerViewModel = hiltViewModel()
    val navUiState = viewModel.uiState.collectAsState()
    Text(stringResource(R.string.my_subreddits), modifier = Modifier.padding(16.dp))
    Row() {
        Text(text = stringResource(R.string.logout))
        Button(onClick = { viewModel.logOut() }) {
            Icon(imageVector = Icons.Rounded.Clear, contentDescription = null)
        }
    }
    Divider()
    NavigationDrawerItem(
        label = { Text(text = stringResource(R.string.frontpage)) },
        selected = false,
        onClick = {
            onSubredditClick(null, "Frontpage")
        }
    )
    NavigationDrawerItem(
        label = { Text(text = stringResource(R.string.all)) },
        selected = false,
        onClick = {
            onSubredditClick("/r/all", "All")
        }
    )
    Divider()
    LazyColumn {
        items(navUiState.value.subscribedSubreddits) {
            NavigationDrawerItem(
                label = { Text(text = it.displayName) },
                selected = false,
                onClick = {
                    onSubredditClick(it.url, it.displayName)
                }
            )
        }

    }
}
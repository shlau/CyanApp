package com.example.redditapp.ui.screens.subreddit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Sort
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Footer(openDrawerState: () -> Unit, modifier: Modifier = Modifier) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Button(onClick = openDrawerState) {
            Icon(imageVector = Icons.Rounded.List, contentDescription = null)
        }
        Button(onClick = {}) {
            Icon(imageVector = Icons.Rounded.Refresh, contentDescription = null)
        }
        Button(onClick = {}) {
            Icon(imageVector = Icons.Rounded.Sort, contentDescription = null)
        }
    }
}

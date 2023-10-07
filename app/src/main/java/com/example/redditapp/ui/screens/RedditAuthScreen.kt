package com.example.redditapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.redditapp.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun RedditAuthScreen(modifier: Modifier = Modifier) {
    val viewModel: RedditAuthViewModel = viewModel()
    val authUiState by viewModel.uiState.collectAsState()
    val uriHandler = LocalUriHandler.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .background(color = Color.White),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Enter your reddit api key",
            textAlign = TextAlign.Justify,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        OutlinedTextField(
            value = authUiState.apiKey,
            onValueChange = { viewModel.updateApiKey(it) },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = { viewModel.openRedditLogin(authUiState.apiKey, uriHandler) },
            enabled = authUiState.apiKey !== "",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        ) {
            Text(text = stringResource(R.string.submit))
        }
    }
}

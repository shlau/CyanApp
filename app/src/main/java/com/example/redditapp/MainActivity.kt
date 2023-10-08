package com.example.redditapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.redditapp.ui.screens.RedditAppScreen
import com.example.redditapp.ui.screens.auth.RedditAuthScreen
import com.example.redditapp.ui.theme.RedditAppTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RedditAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RedditAppScreen()
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun RedditAppPreview() {
    RedditAppTheme {
        RedditAuthScreen()
    }
}
package com.example.redditapp.ui.screens

import android.content.Intent
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.redditapp.ui.screens.auth.RedditAuthScreen
import com.example.redditapp.ui.screens.subreddit.SubredditPage

@Composable
fun RedditAppScreen(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    val viewModel: RedditAppViewModel = viewModel()
    val userToken = viewModel.userTokenFlow.collectAsStateWithLifecycle(initialValue = "")

    NavHost(navController = navController, startDestination = "auth") {
        composable(route = "home") {
            SubredditPage()
        }
        composable(route = "auth") {
            Log.d("RedditApi", userToken.value)
            if (userToken.value === "") {
                RedditAuthScreen()
            } else {
                SubredditPage()
            }
        }
        composable(
            route = "login",
            deepLinks = listOf(navDeepLink {
                uriPattern = "cyan://reddit#{code}"
                action = Intent.ACTION_VIEW
            }),
            arguments = listOf(navArgument("code") {
                type = NavType.StringType
                defaultValue = ""
            })
        ) { navBackStackEntry ->
            val query = navBackStackEntry.arguments?.getString("code")
            val params = query?.split("=")
            var token = ""
            if (params != null) {
                val tokenStr = params[1]
                token = tokenStr.split("&")[0]
                token = "bearer $token"

            }
            Log.d("RedditApi", "token received $token")
            viewModel.updateToken(token)
            SubredditPage()
        }
    }
}

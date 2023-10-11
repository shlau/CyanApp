package com.example.redditapp.ui.screens

import android.content.Intent
import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.example.redditapp.R
import com.example.redditapp.ui.screens.auth.RedditAuthScreen
import com.example.redditapp.ui.screens.subreddit.SubredditPage

enum class NavRoutes(@StringRes title: Int) {
    Auth(R.string.auth),
    Login(R.string.login),
    Home(R.string.home)
}

@Composable
fun RedditAppScreen(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    val viewModel: RedditAppViewModel = viewModel()
    val userToken = viewModel.userTokenFlow.collectAsStateWithLifecycle(initialValue = "")
    val tokenExpirationFlow =
        viewModel.tokenExpirationFlow.collectAsStateWithLifecycle(initialValue = "")
    val redditAppUiState = viewModel.uiState.collectAsState()

    NavHost(navController = navController, startDestination = NavRoutes.Auth.name) {
        composable(route = NavRoutes.Home.name) {
            SubredditPage()
        }
        composable(route = NavRoutes.Auth.name) {
            Log.d("RedditApi", userToken.value)

            val tokenExpirationStr = tokenExpirationFlow.value
            if (tokenExpirationStr != "") {
                val currentTimestamp: Long = System.currentTimeMillis()
                val expirationTimestamp: Long = tokenExpirationStr.toLong()
                if (expirationTimestamp > currentTimestamp) {
                    Log.d("RedditApi", "token expired")
                    viewModel.refreshAccessToken()
                } else {
                    SubredditPage()
                }
            } else if (userToken.value == "") {
                RedditAuthScreen()
            } else {
                SubredditPage()
            }
        }
        composable(
            route = NavRoutes.Login.name,
            deepLinks = listOf(navDeepLink {
                uriPattern = "cyan://reddit?{params}"
                action = Intent.ACTION_VIEW
            }),
            arguments = listOf(navArgument("params") {
                type = NavType.StringType
                defaultValue = ""
            })
        ) { navBackStackEntry ->
            if (userToken.value != "") {
                SubredditPage()
                Log.d("RedditApi", "token exists ${userToken.value}")
            } else {
                val queryParamString = navBackStackEntry.arguments?.getString("params")
                val paramMapping = viewModel.getParamMapping(queryParamString ?: "")
                val code = paramMapping["code"]
                val clientId = paramMapping["state"]
                if (code != null && clientId != null && redditAppUiState.value.code == "") {
                    viewModel.updateCode(code)
                    viewModel.getAccessResponse(code, clientId)
                }
            }
        }
    }
}

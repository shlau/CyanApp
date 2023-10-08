package com.example.redditapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.redditapp.ui.screens.sidebar.NavDrawer
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun Homepage(modifier: Modifier = Modifier) {
    var drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(drawerState = drawerState, drawerContent = {
        ModalDrawerSheet {
            NavDrawer()
        }
    }) {
        Scaffold(bottomBar = {
            Footer({
                scope.launch {
                    drawerState.apply {
                        if (isClosed) {
                            open()
                        } else {
                            close()
                        }
                    }
                }
            })
        }) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                Text(text = "Homepage")
            }
        }
    }
}


package com.objectorientedoleg.githubrepos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.objectorientedoleg.githubrepos.feature.topstarredrepos.navigation.TopStarredReposRoute
import com.objectorientedoleg.githubrepos.feature.topstarredrepos.navigation.topStarredReposScreen
import com.objectorientedoleg.githubrepos.ui.theme.GitHubReposTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GitHubReposTheme {
                NavHost(
                    navController = rememberNavController(),
                    startDestination = TopStarredReposRoute
                ) {
                    topStarredReposScreen()
                }
            }
        }
    }
}
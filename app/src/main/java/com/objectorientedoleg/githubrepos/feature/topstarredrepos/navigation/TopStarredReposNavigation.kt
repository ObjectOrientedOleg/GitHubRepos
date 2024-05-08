package com.objectorientedoleg.githubrepos.feature.topstarredrepos.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.objectorientedoleg.githubrepos.feature.topstarredrepos.TopStarredReposRoute

const val TopStarredReposRoute = "top_starred_repos_route"

fun NavGraphBuilder.topStarredReposScreen() {
    composable(
        route = TopStarredReposRoute
    ) {
        TopStarredReposRoute()
    }
}
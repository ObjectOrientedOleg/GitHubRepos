package com.objectorientedoleg.githubrepos.feature.topstarredrepos

import androidx.compose.runtime.Immutable

@Immutable
sealed interface TopStarredReposUiState {
    data object Loading : TopStarredReposUiState
    data class Success(val items: RepositoryItems) : TopStarredReposUiState
    data object Error : TopStarredReposUiState
}

@Immutable
class RepositoryItems(items: List<RepositoryItem>) : List<RepositoryItem> by items

/**
 * UI layer representation of a GitHub repository, with a [TopContributor].
 */
data class RepositoryItem(
    val id: Int,
    val name: String,
    val description: String?,
    val language: String?,
    val avatarUrl: String?,
    val starCount: String,
    val rank: String,
    val topContributor: TopContributor
)

@Immutable
sealed interface TopContributor {
    data object Loading : TopContributor
    data class Success(val topContributor: String) : TopContributor
    data object Error : TopContributor
}
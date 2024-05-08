package com.objectorientedoleg.githubrepos.feature.topstarredrepos

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class UiStatePreviewParameterProvider : PreviewParameterProvider<TopStarredReposUiState> {
    override val values: Sequence<TopStarredReposUiState> = sequenceOf(
        TopStarredReposUiState.Loading,
        TopStarredReposUiState.Success(
            RepositoryItems(
                listOf(
                    RepositoryItem(
                        id = 1,
                        name = "Repository 1",
                        description = "Description 1",
                        language = "Kotlin",
                        avatarUrl = null,
                        starCount = "100",
                        rank = "1",
                        topContributor = TopContributor.Success("Contributor 1"),
                    ),
                    RepositoryItem(
                        id = 2,
                        name = "Repository 2",
                        description = "Description 2",
                        language = "Kotlin",
                        avatarUrl = null,
                        starCount = "100",
                        rank = "2",
                        topContributor = TopContributor.Success("Contributor 2"),
                    ),
                    RepositoryItem(
                        id = 3,
                        name = "Repository 3",
                        description = "Description 3",
                        language = "Kotlin",
                        avatarUrl = null,
                        starCount = "100",
                        rank = "3",
                        topContributor = TopContributor.Success("Contributor 3"),
                    ),
                )
            )
        ),
        TopStarredReposUiState.Error,
    )
}
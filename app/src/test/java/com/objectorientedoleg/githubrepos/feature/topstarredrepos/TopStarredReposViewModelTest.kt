package com.objectorientedoleg.githubrepos.feature.topstarredrepos

import app.cash.turbine.test
import com.objectorientedoleg.githubrepos.MainDispatcherRule
import com.objectorientedoleg.githubrepos.data.GitHubReposRepository
import com.objectorientedoleg.githubrepos.model.GitHubContributor
import com.objectorientedoleg.githubrepos.model.GitHubRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class TopStarredReposViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val successfulRepository = object : GitHubReposRepository {
        override fun getTopHundredStarredRepos(): Flow<List<GitHubRepository>> =
            flow {
                emit(
                    listOf(
                        GitHubRepository(
                            id = 1,
                            name = "name",
                            fullName = "fullName",
                            owner = GitHubRepository.Owner(
                                id = 1,
                                name = "name",
                                avatarUrl = null
                            ),
                            description = null,
                            language = null,
                            starCount = 0
                        )
                    )
                )
            }

        override fun getTopContributor(repository: GitHubRepository): Flow<GitHubContributor?> =
            flow {
                emit(
                    GitHubContributor(
                        id = 1,
                        name = "name",
                        contributions = 0
                    )
                )
            }
    }

    private val failedRepository = object : GitHubReposRepository {
        override fun getTopHundredStarredRepos(): Flow<List<GitHubRepository>> =
            flow { emit(emptyList()) }

        override fun getTopContributor(repository: GitHubRepository): Flow<GitHubContributor?> =
            flow { emit(null) }
    }

    private lateinit var sut: TopStarredReposViewModel

    @Test
    fun `success ui state is emitted when repository returns repos`() = runTest {
        sut = TopStarredReposViewModel(successfulRepository)
        sut.uiState.test {
            assertTrue(awaitItem() is TopStarredReposUiState.Loading)
            val uiState = awaitItem()
            assertTrue(uiState is TopStarredReposUiState.Success)
            assertTrue((uiState as TopStarredReposUiState.Success).items.isNotEmpty())
        }
    }

    @Test
    fun `top contributors are emitted when repository returns contributors`() = runTest {
        sut = TopStarredReposViewModel(successfulRepository)
        sut.uiState.test {
            skipItems(2)
            val uiState = awaitItem()
            assertTrue(uiState is TopStarredReposUiState.Success)
            val item = (uiState as TopStarredReposUiState.Success).items.first()
            assertTrue(item.topContributor is TopContributor.Success)
        }
    }

    @Test
    fun `error ui state is emitted when repository returns empty list`() = runTest {
        sut = TopStarredReposViewModel(failedRepository)
        sut.uiState.test {
            assertTrue(awaitItem() is TopStarredReposUiState.Loading)
            assertTrue(awaitItem() is TopStarredReposUiState.Error)
        }
    }
}
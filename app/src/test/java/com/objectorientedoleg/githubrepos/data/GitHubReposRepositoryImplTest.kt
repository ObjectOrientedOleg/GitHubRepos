package com.objectorientedoleg.githubrepos.data

import app.cash.turbine.test
import com.objectorientedoleg.githubrepos.model.GitHubRepository
import com.objectorientedoleg.githubrepos.network.GitHubReposNetworkDataSource
import com.objectorientedoleg.githubrepos.network.model.NetworkGitHubContributor
import com.objectorientedoleg.githubrepos.network.model.NetworkGitHubRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

class GitHubReposRepositoryImplTest {

    private val successfulNetworkDataSource = object : GitHubReposNetworkDataSource {
        override suspend fun searchRepositories(
            query: String,
            count: Int
        ): Result<List<NetworkGitHubRepository>> = Result.success(
            listOf(
                NetworkGitHubRepository(
                    id = 1,
                    name = "name",
                    fullName = "fullName",
                    owner = NetworkGitHubRepository.Owner(
                        id = 1,
                        login = "login",
                        avatarUrl = null
                    ),
                    description = null,
                    language = null,
                    starCount = 0
                )
            )
        )

        override suspend fun getContributors(
            owner: String,
            repo: String
        ): Result<List<NetworkGitHubContributor>> = Result.success(
            listOf(
                NetworkGitHubContributor(
                    id = 1,
                    login = "login",
                    contributions = 0
                ),
                NetworkGitHubContributor(
                    id = 2,
                    login = "login",
                    contributions = 0
                )
            )
        )
    }

    private val failedNetworkDataSource = object : GitHubReposNetworkDataSource {
        override suspend fun searchRepositories(
            query: String,
            count: Int
        ): Result<List<NetworkGitHubRepository>> = Result.failure(Exception())

        override suspend fun getContributors(
            owner: String,
            repo: String
        ): Result<List<NetworkGitHubContributor>> = Result.failure(Exception())
    }

    private lateinit var sut: GitHubReposRepositoryImpl

    @Test
    fun `list of repositories is emitted when network data source is successful`() = runTest {
        sut = GitHubReposRepositoryImpl(successfulNetworkDataSource)
        sut.getTopHundredStarredRepos().test {
            assertTrue(awaitItem().isNotEmpty())
            awaitComplete()
        }
    }

    @Test
    fun `top contributor is emitted when network data source is successful`() = runTest {
        sut = GitHubReposRepositoryImpl(successfulNetworkDataSource)
        sut.getTopContributor(fakeRepository()).test {
            val contributor = awaitItem()
            assertNotNull(contributor)
            assertEquals(1, contributor!!.id)
            awaitComplete()
        }
    }

    @Test
    fun `empty list is emitted when network data source has failed`() = runTest {
        sut = GitHubReposRepositoryImpl(failedNetworkDataSource)
        sut.getTopHundredStarredRepos().test {
            assertTrue(awaitItem().isEmpty())
            awaitComplete()
        }
    }

    @Test
    fun `null is emitted when network data source has failed`() = runTest {
        sut = GitHubReposRepositoryImpl(failedNetworkDataSource)
        sut.getTopContributor(fakeRepository()).test {
            assertNull(awaitItem())
            awaitComplete()
        }
    }

    private fun fakeRepository() = GitHubRepository(
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
}
package com.objectorientedoleg.githubrepos.data

import app.cash.turbine.test
import com.objectorientedoleg.githubrepos.database.dao.GitHubContributorDao
import com.objectorientedoleg.githubrepos.database.dao.GitHubRepositoryDao
import com.objectorientedoleg.githubrepos.database.dao.QueryDao
import com.objectorientedoleg.githubrepos.database.model.*
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

    private val fakeContributorDao = object : GitHubContributorDao {
        override suspend fun insert(entity: GitHubContributorEntity) {}
    }

    private val fakeRepositoryDao = object : GitHubRepositoryDao {
        override suspend fun insert(entities: List<GitHubRepositoryEntity>) {}
    }

    private val successfulQueryDao = object : QueryDao {
        override suspend fun insert(entity: QueryEntity) {}

        override suspend fun insert(entity: QueryAndContributorEntity) {}

        override suspend fun insert(entities: List<QueryAndRepositoryEntity>) {}

        override suspend fun getQueryWithRepositories(query: String): QueryWithRepositories =
            QueryWithRepositories(
                query = QueryEntity(query),
                repositories = listOf(
                    GitHubRepositoryEntity(
                        repositoryId = 1,
                        name = "name",
                        fullName = "fullName",
                        owner = GitHubRepositoryEntity.Owner(
                            ownerId = 1,
                            name = "name",
                            avatarUrl = null
                        ),
                        description = null,
                        language = null,
                        starCount = 0
                    )
                )
            )

        override suspend fun getQueryWithContributors(query: String): QueryWithContributors =
            QueryWithContributors(
                query = QueryEntity(query),
                contributor = GitHubContributorEntity(
                    contributorId = 1,
                    name = "name",
                    contributions = 0
                )
            )

        override suspend fun deleteQueryWithContributors(query: String) {}

        override suspend fun deleteQueryWithRepositories(query: String) {}
    }

    private val failedQueryDao = object : QueryDao {
        override suspend fun insert(entity: QueryEntity) {}

        override suspend fun insert(entity: QueryAndContributorEntity) {}

        override suspend fun insert(entities: List<QueryAndRepositoryEntity>) {}

        override suspend fun getQueryWithRepositories(query: String): QueryWithRepositories? = null

        override suspend fun getQueryWithContributors(query: String): QueryWithContributors =
            QueryWithContributors(
                query = QueryEntity(query, 0),
                contributor = GitHubContributorEntity(
                    contributorId = 2,
                    name = "name",
                    contributions = 0
                )
            )

        override suspend fun deleteQueryWithContributors(query: String) {}

        override suspend fun deleteQueryWithRepositories(query: String) {}
    }

    private lateinit var sut: GitHubReposRepositoryImpl

    @Test
    fun `list of repositories is emitted when network data source is successful`() = runTest {
        sut = GitHubReposRepositoryImpl(
            successfulNetworkDataSource,
            fakeContributorDao,
            fakeRepositoryDao,
            failedQueryDao
        )
        sut.getTopHundredStarredRepos().test {
            assertTrue(awaitItem().isNotEmpty())
            awaitComplete()
        }
    }

    @Test
    fun `top contributor is emitted when network data source is successful`() = runTest {
        sut = GitHubReposRepositoryImpl(
            successfulNetworkDataSource,
            fakeContributorDao,
            fakeRepositoryDao,
            failedQueryDao
        )
        sut.getTopContributor(fakeRepository()).test {
            val contributor = awaitItem()
            assertNotNull(contributor)
            assertEquals(1, contributor!!.id)
            awaitComplete()
        }
    }

    @Test
    fun `list of repositories is emitted when local data source has unexpired cache`() = runTest {
        sut = GitHubReposRepositoryImpl(
            failedNetworkDataSource,
            fakeContributorDao,
            fakeRepositoryDao,
            successfulQueryDao
        )
        sut.getTopHundredStarredRepos().test {
            assertTrue(awaitItem().isNotEmpty())
            awaitComplete()
        }
    }

    @Test
    fun `top contributor is emitted when local data source has unexpired cache`() = runTest {
        sut = GitHubReposRepositoryImpl(
            failedNetworkDataSource,
            fakeContributorDao,
            fakeRepositoryDao,
            successfulQueryDao
        )
        sut.getTopContributor(fakeRepository()).test {
            val contributor = awaitItem()
            assertNotNull(contributor)
            assertEquals(1, contributor!!.id)
            awaitComplete()
        }
    }

    @Test
    fun `empty list is emitted when network has failed and no local cache`() = runTest {
        sut = GitHubReposRepositoryImpl(
            failedNetworkDataSource,
            fakeContributorDao,
            fakeRepositoryDao,
            failedQueryDao
        )
        sut.getTopHundredStarredRepos().test {
            assertTrue(awaitItem().isEmpty())
            awaitComplete()
        }
    }

    @Test
    fun `null is emitted when network has failed and local cache expired`() = runTest {
        sut = GitHubReposRepositoryImpl(
            failedNetworkDataSource,
            fakeContributorDao,
            fakeRepositoryDao,
            failedQueryDao
        )
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
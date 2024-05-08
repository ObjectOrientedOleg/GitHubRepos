package com.objectorientedoleg.githubrepos.data

import com.objectorientedoleg.githubrepos.database.dao.GitHubContributorDao
import com.objectorientedoleg.githubrepos.database.dao.GitHubRepositoryDao
import com.objectorientedoleg.githubrepos.database.dao.QueryDao
import com.objectorientedoleg.githubrepos.database.model.*
import com.objectorientedoleg.githubrepos.model.GitHubContributor
import com.objectorientedoleg.githubrepos.model.GitHubRepository
import com.objectorientedoleg.githubrepos.network.GitHubReposNetworkDataSource
import com.objectorientedoleg.githubrepos.network.model.NetworkGitHubContributor
import com.objectorientedoleg.githubrepos.network.model.NetworkGitHubRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

private const val TopStarredReposQuery = "stars:>0"

class GitHubReposRepositoryImpl @Inject constructor(
    private val networkDataSource: GitHubReposNetworkDataSource,
    private val contributorDao: GitHubContributorDao,
    private val repositoryDao: GitHubRepositoryDao,
    private val queryDao: QueryDao,
) : GitHubReposRepository {

    override fun getTopHundredStarredRepos(): Flow<List<GitHubRepository>> = flow {
        val cache = queryDao.getQueryWithRepositories(TopStarredReposQuery)
        if (cache == null || cache.query.isExpired() || cache.repositories.isEmpty()) {
            val repositories = fetchFromNetwork(TopStarredReposQuery, 100)
                .also { persistLocally(TopStarredReposQuery, it) }
            emit(repositories)
        } else {
            val repositories = cache.repositories
                .asSequence()
                .map(GitHubRepositoryEntity::toGitHubRepository)
                .sortedByDescending { it.starCount }
                .take(100)
                .toList()
            emit(repositories)
        }
    }

    private suspend fun fetchFromNetwork(query: String, count: Int) = networkDataSource
        .searchRepositories(query = query, count = count)
        .getOrNull()
        ?.map(NetworkGitHubRepository::toGitHubRepository)
        .orEmpty()

    private suspend fun persistLocally(query: String, repositories: List<GitHubRepository>) {
        if (repositories.isEmpty()) {
            return
        }
        repositoryDao.insert(repositories.map(GitHubRepository::toEntity))
        queryDao.deleteAndInsert(
            query = query,
            entities = repositories.map { it.toQueryAndRepositoryEntity(query) }
        )
    }

    override fun getTopContributor(repository: GitHubRepository): Flow<GitHubContributor?> = flow {
        val cache = queryDao.getQueryWithContributors(repository.fullName)
        if (cache == null || cache.query.isExpired()) {
            val contributor = fetchFromNetwork(repository)
                .also { persistLocally(repository.fullName, it) }
            emit(contributor)
        } else {
            emit(cache.contributor.toGitHubContributor())
        }
    }

    private suspend fun fetchFromNetwork(repository: GitHubRepository) = networkDataSource
        .getContributors(owner = repository.owner.name, repo = repository.name)
        .getOrNull()
        ?.firstOrNull()
        ?.toGitHubContributor()

    private suspend fun persistLocally(query: String, contributor: GitHubContributor?) {
        if (contributor == null) {
            return
        }
        contributorDao.insert(contributor.toEntity())
        queryDao.deleteAndInsert(
            query = query,
            entity = contributor.toQueryAndContributorEntity(query)
        )
    }
}

private fun QueryEntity.isExpired() = System.currentTimeMillis() - queriedDate >= 1000 * 60 * 15

private fun GitHubContributor.toEntity() = GitHubContributorEntity(
    contributorId = id,
    name = name,
    contributions = contributions
)

private fun GitHubContributor.toQueryAndContributorEntity(query: String) =
    QueryAndContributorEntity(
        query = query,
        contributorId = id
    )

private fun NetworkGitHubContributor.toGitHubContributor() = GitHubContributor(
    id = id,
    name = login,
    contributions = contributions
)

private fun GitHubContributorEntity.toGitHubContributor() = GitHubContributor(
    id = contributorId,
    name = name,
    contributions = contributions
)

private fun GitHubRepository.toEntity() = GitHubRepositoryEntity(
    repositoryId = id,
    name = name,
    fullName = fullName,
    owner = GitHubRepositoryEntity.Owner(
        ownerId = owner.id,
        name = owner.name,
        avatarUrl = owner.avatarUrl
    ),
    description = description,
    language = language,
    starCount = starCount
)

private fun GitHubRepository.toQueryAndRepositoryEntity(query: String) =
    QueryAndRepositoryEntity(
        query = query,
        repositoryId = id
    )

private fun NetworkGitHubRepository.toGitHubRepository() = GitHubRepository(
    id = id,
    name = name,
    fullName = fullName,
    owner = GitHubRepository.Owner(
        id = owner.id,
        name = owner.login,
        avatarUrl = owner.avatarUrl
    ),
    description = description,
    language = language,
    starCount = starCount
)

private fun GitHubRepositoryEntity.toGitHubRepository() = GitHubRepository(
    id = repositoryId,
    name = name,
    fullName = fullName,
    owner = GitHubRepository.Owner(
        id = owner.ownerId,
        name = owner.name,
        avatarUrl = owner.avatarUrl
    ),
    description = description,
    language = language,
    starCount = starCount
)
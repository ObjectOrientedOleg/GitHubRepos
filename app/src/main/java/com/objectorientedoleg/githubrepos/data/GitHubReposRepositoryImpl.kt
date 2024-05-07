package com.objectorientedoleg.githubrepos.data

import com.objectorientedoleg.githubrepos.model.GitHubContributor
import com.objectorientedoleg.githubrepos.model.GitHubRepository
import com.objectorientedoleg.githubrepos.network.GitHubReposNetworkDataSource
import com.objectorientedoleg.githubrepos.network.model.NetworkGitHubContributor
import com.objectorientedoleg.githubrepos.network.model.NetworkGitHubRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GitHubReposRepositoryImpl @Inject constructor(
    private val networkDataSource: GitHubReposNetworkDataSource,
) : GitHubReposRepository {

    override fun getTopHundredStarredRepos(): Flow<List<GitHubRepository>> = flow {
        val repositories = networkDataSource
            .searchRepositories(query = "stars:>0", count = 100)
            .getOrNull()
            ?.map(NetworkGitHubRepository::toGitHubRepository)
            .orEmpty()
        emit(repositories)
    }

    override fun getTopContributor(repository: GitHubRepository): Flow<GitHubContributor?> = flow {
        val contributor = networkDataSource
            .getContributors(owner = repository.owner.name, repo = repository.name)
            .getOrNull()
            ?.firstOrNull()
            ?.toGitHubContributor()
        emit(contributor)
    }
}

private fun NetworkGitHubRepository.toGitHubRepository() = GitHubRepository(
    id = id,
    name = name,
    owner = GitHubRepository.Owner(
        id = owner.id,
        name = owner.login,
        avatarUrl = owner.avatarUrl
    ),
    description = description,
    language = language,
    starCount = starCount
)

private fun NetworkGitHubContributor.toGitHubContributor() = GitHubContributor(
    id = id,
    name = login,
    contributions = contributions
)
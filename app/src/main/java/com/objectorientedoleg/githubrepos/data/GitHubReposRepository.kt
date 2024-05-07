package com.objectorientedoleg.githubrepos.data

import com.objectorientedoleg.githubrepos.model.GitHubContributor
import com.objectorientedoleg.githubrepos.model.GitHubRepository
import kotlinx.coroutines.flow.Flow

/**
 * Represents a repository for accessing GitHub repository data.
 */
interface GitHubReposRepository {
    /**
     * Retrieves the top 100 starred repositories from GitHub.
     *
     * @return A [Flow] emitting a list of [GitHubRepository] objects representing the top 100 starred repositories.
     *         The list is sorted in descending order based on the number of stars.
     */
    fun getTopHundredStarredRepos(): Flow<List<GitHubRepository>>

    /**
     * Retrieves the top contributor for the specified GitHub repository.
     *
     * @param repository The [GitHubRepository] object representing the repository for which to retrieve the top contributor.
     * @return A [Flow] emitting a nullable [GitHubContributor] object representing the top contributor for the specified repository.
     *         If the repository has no contributors or the data is not available, the flow emits `null`.
     */
    fun getTopContributor(repository: GitHubRepository): Flow<GitHubContributor?>
}
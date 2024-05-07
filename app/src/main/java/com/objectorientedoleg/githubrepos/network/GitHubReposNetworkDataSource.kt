package com.objectorientedoleg.githubrepos.network

import com.objectorientedoleg.githubrepos.network.model.NetworkGitHubContributor
import com.objectorientedoleg.githubrepos.network.model.NetworkGitHubRepository

/**
 * Represents a network data source for accessing GitHub repository data.
 */
interface GitHubReposNetworkDataSource {
    /**
     * Searches for GitHub repositories based on the provided query.
     *
     * @param query The search query string.
     * @param count The maximum number of repositories to return.
     * @return A [Result] object containing a list of [NetworkGitHubRepository] objects representing the search results.
     *         If the search is successful, the [Result] will be of type [Result.Success] and contain the list of repositories.
     *         If an error occurs during the search, the [Result] will be of type [Result.Failure] and contain the error details.
     */
    suspend fun searchRepositories(query: String, count: Int): Result<List<NetworkGitHubRepository>>

    /**
     * Retrieves the contributors for a specific GitHub repository.
     *
     * @param owner The username of the repository owner.
     * @param repo The name of the repository.
     * @return A [Result] object containing a list of [NetworkGitHubContributor] objects representing the contributors of the repository.
     *         If the retrieval is successful, the [Result] will be of type [Result.Success] and contain the list of contributors.
     *         If an error occurs during the retrieval, the [Result] will be of type [Result.Failure] and contain the error details.
     */
    suspend fun getContributors(owner: String, repo: String): Result<List<NetworkGitHubContributor>>
}
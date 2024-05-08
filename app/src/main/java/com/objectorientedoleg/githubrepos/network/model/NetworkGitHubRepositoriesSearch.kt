package com.objectorientedoleg.githubrepos.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a search result of GitHub repositories retrieved from the network.
 */
@Serializable
data class NetworkGitHubRepositoriesSearch(
    @SerialName("items") val repositories: List<NetworkGitHubRepository>
)

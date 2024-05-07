package com.objectorientedoleg.githubrepos.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkGitHubRepositoriesSearch(
    @SerialName("items") val repositories: List<NetworkGitHubRepository>
)

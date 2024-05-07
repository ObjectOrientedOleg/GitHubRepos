package com.objectorientedoleg.githubrepos.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkGitHubContributor(
    @SerialName("id") val id: Int,
    @SerialName("login") val login: String,
    @SerialName("contributions") val contributions: Int,
)

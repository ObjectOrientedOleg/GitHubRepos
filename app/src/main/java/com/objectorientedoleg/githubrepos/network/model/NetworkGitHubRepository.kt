package com.objectorientedoleg.githubrepos.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkGitHubRepository(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("full_name") val fullName: String,
    @SerialName("owner") val owner: Owner,
    @SerialName("description") val description: String?,
    @SerialName("language") val language: String?,
    @SerialName("stargazers_count") val starCount: Int,
) {
    @Serializable
    data class Owner(
        @SerialName("id") val id: Int,
        @SerialName("login") val login: String,
        @SerialName("avatar_url") val avatarUrl: String?,
    )
}

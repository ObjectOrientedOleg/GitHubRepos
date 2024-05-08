package com.objectorientedoleg.githubrepos.model

data class GitHubRepository(
    val id: Int,
    val name: String,
    val fullName: String,
    val owner: Owner,
    val description: String?,
    val language: String?,
    val starCount: Int,
) {
    data class Owner(
        val id: Int,
        val name: String,
        val avatarUrl: String?,
    )
}

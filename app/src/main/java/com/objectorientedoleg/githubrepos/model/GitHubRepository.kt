package com.objectorientedoleg.githubrepos.model

/**
 * Domain layer representation of a GitHub repository.
 */
data class GitHubRepository(
    val id: Int,
    val name: String,
    val fullName: String,
    val owner: Owner,
    val description: String?,
    val language: String?,
    val starCount: Int,
) {
    /**
     * Represents the owner of a GitHub repository.
     */
    data class Owner(
        val id: Int,
        val name: String,
        val avatarUrl: String?,
    )
}

package com.objectorientedoleg.githubrepos.model

/**
 * Domain layer representation of a contributor to a GitHub repository.
 */
data class GitHubContributor(
    val id: Int,
    val name: String,
    val contributions: Int,
)

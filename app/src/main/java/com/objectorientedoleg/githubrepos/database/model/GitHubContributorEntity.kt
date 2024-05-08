package com.objectorientedoleg.githubrepos.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Database entity representing a GitHub contributor.
 */
@Entity
data class GitHubContributorEntity(
    @PrimaryKey val contributorId: Int,
    val name: String,
    val contributions: Int,
)
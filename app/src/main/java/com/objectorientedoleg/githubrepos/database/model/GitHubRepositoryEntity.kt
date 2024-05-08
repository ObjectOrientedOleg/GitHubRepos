package com.objectorientedoleg.githubrepos.database.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Database entity representing a GitHub repository.
 */
@Entity
data class GitHubRepositoryEntity(
    @PrimaryKey val repositoryId: Int,
    val name: String,
    val fullName: String,
    @Embedded val owner: Owner,
    val description: String?,
    val language: String?,
    val starCount: Int,
) {
    data class Owner(
        val ownerId: Int,
        @ColumnInfo(name = "owner_name") val name: String,
        val avatarUrl: String?,
    )
}

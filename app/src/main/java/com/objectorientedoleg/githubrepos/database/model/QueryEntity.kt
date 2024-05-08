package com.objectorientedoleg.githubrepos.database.model

import androidx.room.*

/**
 * Database entity representing a query made for repositories and contributors.
 */
@Entity
data class QueryEntity(
    @PrimaryKey val query: String,
    val queriedDate: Long = System.currentTimeMillis(),
)

@Entity(primaryKeys = ["query", "repositoryId"])
data class QueryAndRepositoryEntity(
    val query: String,
    val repositoryId: Int,
)

data class QueryWithRepositories(
    @Embedded val query: QueryEntity,
    @Relation(
        parentColumn = "query",
        entityColumn = "repositoryId",
        associateBy = Junction(QueryAndRepositoryEntity::class)
    )
    val repositories: List<GitHubRepositoryEntity>,
)

@Entity(primaryKeys = ["query", "contributorId"])
data class QueryAndContributorEntity(
    val query: String,
    val contributorId: Int,
)

data class QueryWithContributors(
    @Embedded val query: QueryEntity,
    @Relation(
        parentColumn = "query",
        entityColumn = "contributorId",
        associateBy = Junction(QueryAndContributorEntity::class)
    )
    val contributor: GitHubContributorEntity,
)

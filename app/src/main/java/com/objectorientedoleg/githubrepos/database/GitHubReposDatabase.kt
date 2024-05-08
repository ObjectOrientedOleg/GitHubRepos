package com.objectorientedoleg.githubrepos.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.objectorientedoleg.githubrepos.database.dao.GitHubContributorDao
import com.objectorientedoleg.githubrepos.database.dao.GitHubRepositoryDao
import com.objectorientedoleg.githubrepos.database.dao.QueryDao
import com.objectorientedoleg.githubrepos.database.model.*

/**
 * Database for storing GitHub repositories and contributors.
 */
@Database(
    entities = [
        GitHubContributorEntity::class,
        GitHubRepositoryEntity::class,
        QueryEntity::class,
        QueryAndContributorEntity::class,
        QueryAndRepositoryEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class GitHubReposDatabase : RoomDatabase() {
    /**
     * DAO for GitHub repositories.
     */
    abstract fun gitHubRepositoryDao(): GitHubRepositoryDao

    /**
     * DAO for GitHub contributors.
     */
    abstract fun gitHubContributorDao(): GitHubContributorDao

    /**
     * DAO for queries.
     */
    abstract fun queryDao(): QueryDao
}
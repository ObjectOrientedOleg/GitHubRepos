package com.objectorientedoleg.githubrepos.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.objectorientedoleg.githubrepos.database.model.GitHubRepositoryEntity

/**
 * DAO for GitHub repositories.
 */
@Dao
interface GitHubRepositoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entities: List<GitHubRepositoryEntity>)
}
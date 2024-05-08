package com.objectorientedoleg.githubrepos.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.objectorientedoleg.githubrepos.database.model.GitHubContributorEntity

/**
 * DAO for GitHub contributors.
 */
@Dao
interface GitHubContributorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: GitHubContributorEntity)
}
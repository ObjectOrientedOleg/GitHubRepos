package com.objectorientedoleg.githubrepos.database.dao

import androidx.room.*
import com.objectorientedoleg.githubrepos.database.model.*

/**
 * DAO for queries.
 */
@Dao
interface QueryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: QueryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: QueryAndContributorEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entities: List<QueryAndRepositoryEntity>)

    @Transaction
    @Query("SELECT * FROM QueryEntity WHERE `query` = :query")
    suspend fun getQueryWithRepositories(query: String): QueryWithRepositories?

    @Transaction
    @Query("SELECT * FROM QueryEntity WHERE `query` = :query")
    suspend fun getQueryWithContributors(query: String): QueryWithContributors?

    @Query("DELETE FROM QueryAndContributorEntity WHERE `query` = :query")
    suspend fun deleteQueryWithContributors(query: String)

    @Query("DELETE FROM QueryAndRepositoryEntity WHERE `query` = :query")
    suspend fun deleteQueryWithRepositories(query: String)

    @Transaction
    suspend fun deleteAndInsert(query: String, entity: QueryAndContributorEntity) {
        deleteQueryWithContributors(query)
        insert(QueryEntity(query))
        insert(entity)
    }

    @Transaction
    suspend fun deleteAndInsert(query: String, entities: List<QueryAndRepositoryEntity>) {
        deleteQueryWithRepositories(query)
        insert(QueryEntity(query))
        insert(entities)
    }
}
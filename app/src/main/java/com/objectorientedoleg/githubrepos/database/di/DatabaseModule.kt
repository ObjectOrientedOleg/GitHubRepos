package com.objectorientedoleg.githubrepos.database.di

import android.content.Context
import androidx.room.Room
import com.objectorientedoleg.githubrepos.database.GitHubReposDatabase
import com.objectorientedoleg.githubrepos.database.dao.GitHubContributorDao
import com.objectorientedoleg.githubrepos.database.dao.GitHubRepositoryDao
import com.objectorientedoleg.githubrepos.database.dao.QueryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun providesGitHubReposDatabase(
        @ApplicationContext context: Context
    ): GitHubReposDatabase = Room
        .databaseBuilder(
            context,
            GitHubReposDatabase::class.java,
            "github_repos_database"
        )
        .build()

    @Provides
    fun providesGitHubContributorDao(database: GitHubReposDatabase): GitHubContributorDao =
        database.gitHubContributorDao()

    @Provides
    fun providesGitHubRepositoryDao(database: GitHubReposDatabase): GitHubRepositoryDao =
        database.gitHubRepositoryDao()

    @Provides
    fun providesQueryDao(database: GitHubReposDatabase): QueryDao = database.queryDao()
}
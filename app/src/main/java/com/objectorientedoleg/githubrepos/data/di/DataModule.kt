package com.objectorientedoleg.githubrepos.data.di

import com.objectorientedoleg.githubrepos.data.GitHubReposRepository
import com.objectorientedoleg.githubrepos.data.GitHubReposRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Binds
    @Singleton
    fun bindsGitHubReposRepository(impl: GitHubReposRepositoryImpl): GitHubReposRepository
}